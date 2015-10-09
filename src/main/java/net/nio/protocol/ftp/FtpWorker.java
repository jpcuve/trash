package net.nio.protocol.ftp;

import net.nio.Dispatcher;
import net.nio.MemoryBuffer;
import net.nio.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 3, 2004
 * Time: 9:21:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class FtpWorker extends Worker<FtpRequest, FtpResponse> implements FtpHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(FtpWorker.class);
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(" MMM dd HH:mm ", Locale.ENGLISH);
    public static final long CONNECTION_TIMEOUT = 10000;
    public static final long TRANSFER_TIMEOUT = 20000;
    private long lastRequest = System.currentTimeMillis();
    private long requestTimeout;
    private FtpRequest request = new FtpRequest(this);
    private FtpResponse response = new FtpResponse(this);
    private State state = State.AUTHENTICATION_READY;
    private Type type = Type.IMAGE;
    private Transmission transmission = Transmission.STREAM;
    private Structure structure = Structure.FILE;
    private String initialFolderId;
    private FtpTransferer ftpTransferer = null;
    private String renaming = null;
    private boolean quit = false;

    private static Map<String, MemoryBuffer> files = new ConcurrentHashMap<String, MemoryBuffer>();

    private enum State{
        AUTHENTICATION_READY, AUTHENTICATION_USER_SET, TRANSACTION, DATA_TRANSFER;
    }

    private enum Transmission{
        STREAM, BLOCK, COMPRESSED;
    }

    private enum Type{
        ASCII, EBCDIC, IMAGE, LOCAL;
    }

    private enum Structure{
        FILE, RECORD, PAGE;
    }

    private enum Format{
        NON_PRINT, TELNET, ASA;
    }

    public FtpWorker(Dispatcher dispatcher, SocketChannel socketChannel, long requestTimeout) throws IOException {
        super(dispatcher, socketChannel, Interest.WRITE);
        this.requestTimeout = requestTimeout;
        response.setResponse(220, new String[]{"FTP test", "FTPD"});
    }

    public void pulse() {
        if (requestTimeout > 0){
            long delay = System.currentTimeMillis() - lastRequest;
            if (delay > requestTimeout){
                LOGGER.info("connection timeout, delay without request: " + delay + "ms");
                done();
            }
        }
    }

    public void service(FtpRequest q, FtpResponse r) {
        lastRequest = System.currentTimeMillis();
        if (q.getFtpMethod() != null){
            String parameter = q.getParameter();
            switch(q.getFtpMethod()){
                case USER:
                    state = State.AUTHENTICATION_USER_SET;
                    r.setResponse(331, "Need password");
                    break;
                case PASS:
                    if (state != State.AUTHENTICATION_USER_SET){
                        r.setResponse(503, "Bad sequence of commands");
                     }else {
                        state = State.TRANSACTION;
                        r.setResponse(230, "User logged in, proceed");
                    }
                    break;
                case ABOR:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        if (ftpTransferer != null) ftpTransferer.close();
                        r.setResponse(226, "Aborting data connection");
                    }
                    break;
                case QUIT:
                    r.setResponse(200, "Quitting");
                    quit = true;
                    break;
                case CWD:
                    r.setResponse(200, "Changed working directory to: " + parameter);
                    break;
                case CDUP:
                    r.setResponse(200, "Changed working directory to: " + parameter);
                    break;
                case NLST:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        if (ftpTransferer == null){
                            r.setResponse(503, "No previous 'PASV' or 'PORT'");
                        } else{
                            StringBuffer sb = new StringBuffer(1024);
                            for (String name: files.keySet()){
                                sb.append(name);
                                sb.append("\r\n");
                            }
                            ftpTransferer.transferString(sb.toString());
                            state = State.DATA_TRANSFER;
                            r.setResponse(150, "FILE: (list)");
                        }
                    }
                    break;
                case LIST:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        if (ftpTransferer == null){
                            r.setResponse(503, "No previous 'PASV' or 'PORT'");
                        } else{
                            StringBuffer sb = new StringBuffer(1024);
                            for (Map.Entry<String, MemoryBuffer> entry: files.entrySet()){
                                sb.append('-');
                                sb.append("rw-r--r--");
                                sb.append(' ');
                                sb.append("root");
                                sb.append(' ');
                                sb.append("root");
                                String size = Integer.toString(entry.getValue().getBuffer().length);
                                sb.append("              ".substring(size.length()));
                                sb.append(size);
                                sb.append(DATE_FORMAT.format(new Date()));
                                sb.append(entry.getKey());
                                sb.append("\r\n");

                            }
                            ftpTransferer.transferString(sb.toString());
                            state = State.DATA_TRANSFER;
                            r.setResponse(150, "FILE: (list)");
                        }
                    }
                    break;
                case MODE:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        switch(parameter.toUpperCase().charAt(0)){
                            case 'S':
                                transmission = Transmission.STREAM;
                                r.setResponse(200, "Mode set to STREAM");
                                break;
                            default:
                                r.setResponse(504, "Command 'MODE' not implemented for that parameter");
                                break;
                        }
                    }
                    break;
                case STRU:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        switch(parameter.toUpperCase().charAt(0)){
                            case 'F':
                                structure = Structure.FILE;
                                r.setResponse(200, "Structure set to FILE");
                                break;
                            default:
                                r.setResponse(504, "Command 'STRU' not implemented for that parameter");
                                break;
                        }
                    }
                    break;
                case TYPE:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        switch(parameter.toUpperCase().charAt(0)){
                            case 'A':
                                type = Type.ASCII;
                                r.setResponse(200, "Type set to ASCII");
                                break;
                            case 'I':
                                type = Type.IMAGE;
                                r.setResponse(200, "Type set to IMAGE");
                                break;
                            default:
                                r.setResponse(504, "Command 'TYPE' not implemented for that parameter");
                                break;
                        }
                    }
                    break;
                case STOR:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        if (ftpTransferer == null){
                            r.setResponse(552, "Cannot receive data (no data transfer process)");
                        } else{
                            MemoryBuffer memoryBuffer = new MemoryBuffer(new byte[32768]);
                            files.put(parameter, memoryBuffer);
                            ftpTransferer.transferMessage(memoryBuffer, "application/octet-stream");
                            state = State.DATA_TRANSFER;
                            r.setResponse(150, "FILE: " + parameter);
                        }
                    }
                    break;
                case STOU:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        if (ftpTransferer == null){
                            r.setResponse(552, "Cannot receive data (no data transfer process)");
                        } else{
                            MemoryBuffer memoryBuffer = new MemoryBuffer(new byte[32768]);
                            files.put(Long.toString(System.currentTimeMillis()), memoryBuffer);
                            ftpTransferer.transferMessage(memoryBuffer, "application/octet-stream");
                            state = State.DATA_TRANSFER;
                            r.setResponse(150, "FILE: " + parameter);
                        }
                    }
                    break;
                case RETR:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        if (ftpTransferer == null){
                            r.setResponse(550, "Cannot send data (no data transfer process)");
                        } else{
                            MemoryBuffer memoryBuffer = files.get(parameter);
                            if (memoryBuffer == null){
                                r.setResponse(550, "Message not found");
                            } else{
                                ftpTransferer.transferMessage(memoryBuffer);
                                state = State.DATA_TRANSFER;
                                r.setResponse(150, "FILE: " + parameter);
                            }
                        }
                    }
                    break;
                case DELE:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        files.remove(parameter);
                        r.setResponse(250, "Node deleted");
                    }
                    break;
                case MKD:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        r.setResponse(550, "Unable to create folder: " + parameter);
                    }
                    break;
                case RMD:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        files.remove(parameter);
                        r.setResponse(250, "Folder deleted");
                    }
                    break;
                case RNFR:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        if (parameter.length() == 0){
                            r.setResponse(553, "Cannot rename '' node");
                        } else{
                            renaming = parameter;
                            r.setResponse(350, "Requested file action pending");
                        }
                    }
                    break;
                case RNTO:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        if (renaming == null){
                            r.setResponse(503, "No previous 'RNFR'");
                        } else{
                            MemoryBuffer memoryBuffer = files.get(renaming);
                            if (memoryBuffer != null){
                                files.remove(renaming);
                                files.put(parameter, memoryBuffer);
                            }
                            renaming = null;
                            r.setResponse(200, "Node renamed");
                        }
                    }
                    break;
                case PASV:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        ftpTransferer = null;
                        try{
                            ftpTransferer = new FtpTransferer(this, dispatcher, CONNECTION_TIMEOUT, TRANSFER_TIMEOUT);
                            String adr = getSocketChannel().socket().getLocalAddress().getHostAddress();
                            adr = adr.replace('.', ',') + ',' + (ftpTransferer.getLocalAddress().getPort() >> 8) + ',' + (ftpTransferer.getLocalAddress().getPort() & 0xFF);
                            r.setResponse(227, "Entering passive mode (" + adr + ")");
                        } catch(IOException x){
                            LOGGER.error("cannot setup passive data connection", x);
                            r.setResponse(550, "Cannot 'PASV' (" + x.getMessage() + ')');
                        }
                    }
                    break;
                case PORT:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        ftpTransferer = null;
                        StringTokenizer st2 = new StringTokenizer(parameter, ",");
                        try{
                            byte[] a = new byte[4];
                            for (int i = 0; i < 4; i++){
                                if (!st2.hasMoreTokens()) throw new IOException("invalid IP address");
                                a[i] = new Integer(st2.nextToken()).byteValue();
                            }
                            if (!st2.hasMoreTokens()) throw new IOException("invalid port high byte");
                            int h = Integer.parseInt(st2.nextToken());
                            if (!st2.hasMoreTokens()) throw new IOException("invalid port low byte");
                            int l = Integer.parseInt(st2.nextToken());
                            ftpTransferer = new FtpTransferer(this, dispatcher, new InetSocketAddress(InetAddress.getByAddress(a), (h << 8) + l), CONNECTION_TIMEOUT, TRANSFER_TIMEOUT);
                            r.setResponse(200, "Port set to " + a[0] + "." + a[1] + "." + a[2] + "." + a[3] + ":" + ((h << 8) + l));
                        } catch(IOException x){
                            LOGGER.error("cannot setup active data connection", x);
                            r.setResponse(550, "Cannot 'PORT' " + parameter + " (" + x.getMessage() + ')');
                        } catch(NumberFormatException x){
                            LOGGER.error("cannot parse port parameters as numbers: " + parameter, x);
                            r.setResponse(501, "Syntax error (" + x.getMessage() + ')');
                        }
                    }
                    break;
                case PWD:
                    if (state != State.TRANSACTION){
                        r.setResponse(503, "Bad sequence of commands");
                    } else{
                        r.setResponse(257, "\"/\" directory");
                    }
                    break;
                case SYST:
                    r.setResponse(215, " UNIX system type");
                    break;
                default:
                    r.setResponse(502, "Command '" + q.getFtpMethod() + "' not implemented");
                    break;
            }
        } else{
            r.setResponse(502, "Command not recognized");
        }
    }

    public void handlePostIn(ByteBuffer inputBuffer) throws IOException {
        request.readFrom(inputBuffer);
    }

    public void handlePreOut(ByteBuffer outputBuffer) throws IOException {
        response.writeTo(outputBuffer);
    }

    public void requestRead(FtpMethod ftpMethod, String parameter) {
        LOGGER.debug("request read: %s %s", ftpMethod, parameter);
        response.reset();
        service(request, response);
        LOGGER.debug("setting interest to write");
        setInterest(Interest.WRITE);
    }

    public void responseWritten(int code, String[] lines){
        LOGGER.debug("response written: {} {}{}", new Object[]{code, lines[0], lines.length > 1 ? "(multiple lines)" : ""});
        if (quit) setInterest(Interest.CLOSE);
        else if (state == State.DATA_TRANSFER) setInterest(Interest.NONE);
        else setInterest(Interest.READ);
        request.reset();
    }

    public void dataTransferComplete(){
        state = State.TRANSACTION;
        response.reset();
        response.setResponse(226, "Closing data connection");
        applyInterest(Interest.WRITE);
    }

    public void dataTransferFailed(Exception x){
        state = State.TRANSACTION;
        response.reset();
        response.setResponse(425, "Unable to transfer data (" + x.getMessage() + ")");
        applyInterest(Interest.WRITE);
    }

}
