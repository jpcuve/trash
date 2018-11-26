package net.nio.protocol.ftp;

import net.nio.Dispatcher;
import net.nio.ErrorHandler;
import net.nio.MemoryBuffer;
import net.nio.SelectorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 4, 2005
 * Time: 3:39:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class FtpTransferer implements SelectorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpTransferer.class);
    private static final Charset CHARSET_ASCII = Charset.forName("US-ASCII");
    private FtpHandler ftpHandler;
    private Dispatcher dispatcher;
    private InetSocketAddress remoteAddress;
    private long connectionTimeout;
    private long transferTimeout;
    private ServerSocketChannel serverSocketChannel = null;
    private SocketChannel socketChannel = null;
    private ReadableByteChannel readableByteChannel = null;
    private WritableByteChannel writableByteChannel = null;
/*
    private Message uploadMessage = null;
    private String uploadName = null;
    private boolean uploadForce = false;
*/
    private ByteBuffer byteBuffer;
    private State state = State.Idle;
    private boolean open = true;
    private long timeConnecting = System.currentTimeMillis();
    private long timeTransferring;

    private enum State{
        Idle, Connecting, Transferring, Data;
    }

    public FtpTransferer(FtpHandler ftpHandler, Dispatcher dispatcher, long connectionTimeout, long transferTimeout) throws IOException {
        this(ftpHandler, dispatcher, 0, 0, connectionTimeout, transferTimeout);
    }

    public FtpTransferer(FtpHandler ftpHandler, Dispatcher dispatcher, int fromPort, int toPort, long connectionTimeout, long transferTimeout) throws IOException {
        this.ftpHandler = ftpHandler;
        this.dispatcher = dispatcher;
        this.remoteAddress = null;
        this.connectionTimeout = connectionTimeout;
        this.transferTimeout = transferTimeout;
        this.serverSocketChannel = ServerSocketChannel.open();
        this.byteBuffer = ByteBuffer.allocateDirect(serverSocketChannel.socket().getReceiveBufferSize() * 2);
        serverSocketChannel.configureBlocking(false);
        if (fromPort == 0 && toPort == 0){
            serverSocketChannel.socket().bind(null);
        } else{
            LOGGER.debug("trying binding from port=" + fromPort + " to=" + toPort);
            boolean bound = false;
            int i = fromPort;
            while(!bound && i <= toPort){
                try{
                    serverSocketChannel.socket().bind(new InetSocketAddress(i));
                    bound = true;
                } catch(BindException ex){
                    LOGGER.debug("unable to bind local transport to port: " + i);
                }
                i++;
            }
            if (!bound){
                LOGGER.info("the whole port range is bound");
                serverSocketChannel.close();
                throw new BindException("unable to bind local transport to port range: " + fromPort + '-' + toPort);
            }
        }
        dispatcher.registerChannelLater(serverSocketChannel, 0, this, new ErrorHandler(){
            public void handleError(Exception x) {
                LOGGER.error("unable to register ftp transferer", x);
            }
        });
    }

    public FtpTransferer(FtpWorker ftpWorker, Dispatcher dispatcher, InetSocketAddress remoteAddress, long connectionTimeout, long transferTimeout) throws IOException {
        this.ftpHandler = ftpWorker;
        this.dispatcher = dispatcher;
        this.remoteAddress = remoteAddress;
        this.connectionTimeout = connectionTimeout;
        this.transferTimeout = transferTimeout;
    }

    public InetSocketAddress getLocalAddress() {
        if (serverSocketChannel != null) return (InetSocketAddress)serverSocketChannel.socket().getLocalSocketAddress();
        if (socketChannel != null) return (InetSocketAddress)socketChannel.socket().getLocalSocketAddress();
        return null;
    }

    public InetSocketAddress getRemoteAddress() {
        return socketChannel == null ? null : (InetSocketAddress)socketChannel.socket().getRemoteSocketAddress();
    }

    public void close(){
        if (open) done();
    }

    public boolean isClosed(){
        return !open;
    }

    private void done(){
        LOGGER.debug("closing ftp data transfer");
        if (readableByteChannel != null) try{
            LOGGER.debug("closing readable byte channel");
            readableByteChannel.close();
        } catch(IOException x){
            LOGGER.error("cannot close readable byte channel", x);
        }
        if (writableByteChannel != null) try{
            LOGGER.debug("closing writable byte channel");
            writableByteChannel.close();
        } catch(IOException x){
            LOGGER.error("cannot close writable byte channel", x);
        }
        if (serverSocketChannel != null) try {
            LOGGER.debug("closing server server socket channel");
            serverSocketChannel.close();
        } catch(IOException x){
            LOGGER.error("cannot close server socket channel", x);
        }
        if (socketChannel != null) try {
            LOGGER.debug("closing socket channel : " + socketChannel.socket().getLocalSocketAddress() + " " + socketChannel.socket().getRemoteSocketAddress());
            socketChannel.close();
        } catch(IOException x){
            LOGGER.error("cannot close socket channel", x);
        }
        open = false;
    }

    private void startConnection(){
        if (remoteAddress == null){
            LOGGER.debug("trying to establish passive connection");
            dispatcher.setChannelInterestLater(serverSocketChannel, SelectionKey.OP_ACCEPT, true, new ErrorHandler(){
                public void handleError(Exception x) {
                    LOGGER.error("unable to register ftp transferer for accept", x);
                }
            });
        } else {
            LOGGER.debug("trying to establish active connection to: " + remoteAddress);
            try{
                socketChannel = SocketChannel.open();
                dispatcher.registerChannelLater(socketChannel, SelectionKey.OP_CONNECT, this, new ErrorHandler(){
                    public void handleError(Exception x) {
                        LOGGER.error("unable to register ftp transferer for connect", x);
                    }
                });
                this.byteBuffer = ByteBuffer.allocateDirect(socketChannel.socket().getReceiveBufferSize());
                LOGGER.debug("connecting socket to=" + remoteAddress);
                socketChannel.configureBlocking(false);
                socketChannel.connect(remoteAddress);
            } catch(IOException x){
                LOGGER.error("cannot connect to remote address", x);
                done();
                failed(x);
            }
        }
        timeConnecting = System.currentTimeMillis();
        state = State.Connecting;
    }

    private void startTransfer(){
        LOGGER.debug("trying to transfer data");
        if (readableByteChannel != null){
            dispatcher.registerChannelLater(socketChannel, SelectionKey.OP_WRITE, this, new ErrorHandler(){
                public void handleError(Exception x) {
                    LOGGER.error("unable to register ftp transferer for write", x);
                }
            });
            LOGGER.debug("data transfer started for writing to socket channel");
        } else if (writableByteChannel != null){
            dispatcher.registerChannelLater(socketChannel, SelectionKey.OP_READ, this, new ErrorHandler(){
                public void handleError(Exception x) {
                    LOGGER.error("unable to register ftp transferer for read", x);
                }
            });
            LOGGER.debug("data transfer started for reading from socket channel");
        }
        timeTransferring = System.currentTimeMillis();
        state = State.Transferring;
    }

    private void complete(){
        ftpHandler.dataTransferComplete();
    }

    private void failed(Exception x){
        ftpHandler.dataTransferFailed(x);
    }

    public void transferMessage(MemoryBuffer memoryBuffer, String type) {
        LOGGER.info("transferring message to server: %d bytes", memoryBuffer.getBuffer().length);
        this.writableByteChannel = memoryBuffer;
        startConnection();
    }

    public void transferMessage(MemoryBuffer memoryBuffer) {
        LOGGER.info("transferring message from server: %d bytes", memoryBuffer.getBuffer().length);
        this.readableByteChannel = memoryBuffer;
        startConnection();
    }

    public void transferString(String data) {
        LOGGER.info("transferring string: " + data);
        this.readableByteChannel = new MemoryBuffer(CHARSET_ASCII.encode(data));
        startConnection();
    }

    public void pulse(){
        switch(state){
            case Idle:
            case Connecting:
                long connectionDelay = System.currentTimeMillis() - timeConnecting;
                if (connectionDelay > connectionTimeout){
                    LOGGER.debug("data connection timeout, closing data channel: " + connectionDelay + "ms");
                    done();
                    failed(new IOException("connection timeout: " + connectionDelay + "ms"));
                }
                break;
            case Transferring:
                long transferDelay = System.currentTimeMillis() - timeTransferring;
                if (transferDelay > transferTimeout){
                    LOGGER.debug("transfer connection timeout, closing data channel: " + transferDelay + "ms");
                    done();
                    failed(new IOException("transfer timeout: " + transferDelay + "ms"));

                }
                break;
            case Data:
                break;
        }
    }

    public void handleAccept() {
        try{
            this.socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            LOGGER.debug("connected passively");
            startTransfer();
        } catch(IOException x){
            LOGGER.error("cannot connect data socket passively", x);
            done();
        }
    }

    public void handleConnect() {
        try{
            if (!socketChannel.finishConnect()) throw new IOException("cannot finish connect");
            LOGGER.debug("connected actively");
            startTransfer();
        } catch(IOException x){
            LOGGER.error("cannot connect data socket actively", x);
            done();
        }
    }

    public void handleRead() {
        state = State.Data;
        try{
            int read = socketChannel.read(byteBuffer); // byteBuffer-put
            // LOGGER.fine("read bytes from data socket channel: " + read);
            if (read == -1 && byteBuffer.position() == 0){
                done();
                state = State.Idle;
                complete();
            } else{
                byteBuffer.flip(); // byteBuffer-flip
                int written = writableByteChannel.write(byteBuffer); // byteBuffer-get
                // LOGGER.fine("wrote bytes: " + written);
                byteBuffer.compact(); // byteBuffer-compact
                dispatcher.setChannelInterestNow(socketChannel, SelectionKey.OP_READ, true);
            }
        } catch(IOException x){
            LOGGER.error("cannot handle read", x);
            done();
            state = State.Idle;
            failed(x);
        }
    }

    public void handleWrite() {
        state = State.Data;
        try{
            int read = readableByteChannel.read(byteBuffer); // byteBuffer-put
            // LOGGER.fine("read bytes: " + read);
            if (read == -1 && byteBuffer.position() == 0){
                done();
                state = State.Idle;
                complete();
            } else{
                byteBuffer.flip(); // byteBuffer-flip
                int written = socketChannel.write(byteBuffer); // byteBuffer-get
                // LOGGER.fine("wrote bytes to data socket channel: " + written);
                byteBuffer.compact(); // byteBuffer-compact
                dispatcher.setChannelInterestNow(socketChannel, SelectionKey.OP_WRITE, true);
            }
        } catch(IOException x){
            LOGGER.error("cannot handle write", x);
            done();
            state = State.Idle;
            failed(x);
        }
    }
}
