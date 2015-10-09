package net.nio.protocol.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 3, 2004
 * Time: 9:20:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class FtpRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpRequest.class);
    private static final Charset CHARSET_ASCII = Charset.forName("US-ASCII");
    private FtpHandler ftpHandler;
    private FtpMethod ftpMethod;
    private String parameter;
    private ByteBuffer buffer;

    public FtpRequest(FtpHandler ftpHandler) {
        this.ftpHandler = ftpHandler;
        reset();
    }

    public FtpMethod getFtpMethod() {
        return ftpMethod;
    }

    public String getParameter() {
        return parameter;
    }

    public void reset() {
        ftpMethod = null;
        parameter = null;
        buffer = null;
    }

    public void readFrom(ByteBuffer inputBuffer) throws IOException {
        try{
            while (inputBuffer.hasRemaining()){
                byte b = inputBuffer.get();
                if (buffer == null) buffer = ByteBuffer.allocate(32768);
                buffer.put(b);
                if (b == 0x0A){
                    buffer.position(buffer.position() - 2);
                    buffer.flip();
                    String line = CHARSET_ASCII.decode(buffer).toString();
                    int sp = line.indexOf(' ');
                    ftpMethod = FtpMethod.parse((sp != -1) ? line.substring(0, sp) : line);
                    parameter = (sp != -1) ? line.substring(sp + 1) : "";
                    ftpHandler.requestRead(ftpMethod, parameter);
                    buffer.clear();
                }
            }
        } catch(Exception x){
            LOGGER.error("cannot read http message", x);
            throw new IOException(x.getMessage());
        }
    }
}
