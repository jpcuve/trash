package net.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 9, 2005
 * Time: 3:03:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SecureWrapper implements SelectorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecureWrapper.class);
    protected Dispatcher dispatcher;
    protected SocketChannel socketChannel;
    private SSLEngine sslEngine;
    private Exchanger exchanger;
    private ByteBuffer outputBuffer;
    private ByteBuffer inputBuffer;
    private ByteBuffer appInputBuffer;
    private ByteBuffer appOutputBuffer;

    public SecureWrapper(Dispatcher dispatcher, SocketChannel socketChannel, SSLEngine sslEngine, Exchanger exchanger) throws IOException {
        this.dispatcher = dispatcher;
        this.socketChannel = socketChannel;
        this.sslEngine = sslEngine;
        this.exchanger = exchanger;
        socketChannel.configureBlocking(false);
        SSLSession sslSession = sslEngine.getSession();
        this.outputBuffer = ByteBuffer.allocate(sslSession.getPacketBufferSize() * 2);
        this.inputBuffer = ByteBuffer.allocate(sslSession.getPacketBufferSize() * 2);
        this.appOutputBuffer = ByteBuffer.allocate(sslSession.getApplicationBufferSize() * 2);
        this.appInputBuffer = ByteBuffer.allocate(sslSession.getApplicationBufferSize() * 2);
        sslEngine.beginHandshake();
    }

    public void open(){
        switch(sslEngine.getHandshakeStatus()){
            case NEED_WRAP:
                LOGGER.debug("handshake begins with need for wrap");
                dispatcher.registerChannelLater(socketChannel, SelectionKey.OP_WRITE, this, new ErrorHandler(){
                    public void handleError(Exception x) {
                        LOGGER.error("unable to register exchanger", x);
                    }
                });
                break;
            case NEED_UNWRAP:
                LOGGER.debug("handshake begins with need for unwrap");
                dispatcher.registerChannelLater(socketChannel, SelectionKey.OP_READ, this, new ErrorHandler(){
                    public void handleError(Exception x) {
                        LOGGER.error("unable to register exchanger", x);
                    }
                });
                break;
            default:
                LOGGER.error("handshake begins with unexpected status: %s, terminating", sslEngine.getHandshakeStatus());
                done();
                break;
        }
    }

    protected void done() {
        if (socketChannel != null) try {
            LOGGER.debug("closing socket channel : " + socketChannel.socket().getLocalSocketAddress() + " " + socketChannel.socket().getRemoteSocketAddress());
            socketChannel.close();
        } catch(IOException x){
            LOGGER.error("cannot close socket channel", x);
        }
    }

    private void exchange() {
        switch(exchanger.getInterest()){
            case NONE:
                break;
            case READ:
                dispatcher.registerChannelLater(socketChannel, SelectionKey.OP_READ, this, new ErrorHandler(){
                    public void handleError(Exception x) {
                        LOGGER.error("unable to register exchanger for 'read'", x);
                        done();
                    }
                });
                break;
            case WRITE:
                dispatcher.registerChannelLater(socketChannel, SelectionKey.OP_WRITE, this, new ErrorHandler(){
                    public void handleError(Exception x) {
                        LOGGER.error("unable to register exchanger for 'write'", x);
                        done();
                    }
                });
                break;
            case CLOSE:
                done();
                break;
            default:
                assert false: exchanger.getInterest();

        }
    }

    public void pulse() {
    }

    public void handleAccept() {
    }

    public void handleConnect() {
    }


    public void handleRead() {
        try{
            int read = socketChannel.read(inputBuffer); // inputBuffer-put
            LOGGER.debug("read bytes from socket channel: %d", read);
            if (read > 0){
                while (sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK || sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP){
                    switch(sslEngine.getHandshakeStatus()){
                        case NEED_TASK:
                            LOGGER.debug("read, running task(s)");
                            Runnable task;
                            long now = System.currentTimeMillis();
                            while ((task = sslEngine.getDelegatedTask()) != null) task.run();
                            LOGGER.info("task duration: %dms", System.currentTimeMillis() - now);
                            break;
                        case NEED_UNWRAP:
                            LOGGER.debug("read, unwrap needed, reading more");
                            if (inputBuffer.position() > 0){
                                inputBuffer.flip(); // inputBufferNet-flip
                                SSLEngineResult result = sslEngine.unwrap(inputBuffer, appInputBuffer); // inputBufferNet-get, inputBuffer-put
                                LOGGER.debug("unwrap result: " + result);
                                inputBuffer.compact();
                                switch(result.getStatus()){
                                    case BUFFER_OVERFLOW:
                                        assert false: "read, buffer overflow";
                                    case BUFFER_UNDERFLOW:
                                        LOGGER.debug("read, underflow");
                                        dispatcher.setChannelInterestNow(socketChannel, SelectionKey.OP_READ, true);
                                        return;
                                    case CLOSED:
                                        LOGGER.debug("read, closed");
                                        sslEngine.closeInbound();
                                        done();
                                        return;
                                    case OK:
                                        // do nothing as no application bytes are generated when handshaking
                                        break;
                                    default:
                                        assert false: result.getStatus();
                                }
                            } else{
                                LOGGER.debug("read, unwrap needed, reading more from socket");
                                dispatcher.setChannelInterestNow(socketChannel, SelectionKey.OP_READ, true);
                                return;
                            }
                            break;
                        default:
                            assert false: sslEngine.getHandshakeStatus();
                    }
                }
                switch(sslEngine.getHandshakeStatus()){
                    case NEED_WRAP:
                        LOGGER.debug("read, wrap needed, switching to writing");
                        dispatcher.setChannelInterestNow(socketChannel, SelectionKey.OP_WRITE, true);
                        break;
                    case FINISHED:
                        LOGGER.info("read, handshake completed, setting initial interest");
                        exchange();
                        break;
                    case NOT_HANDSHAKING:
                        inputBuffer.flip(); // inputBuffer-flip
                        SSLEngineResult result = sslEngine.unwrap(inputBuffer, appInputBuffer); // inputBuffer-get, appInputBuffer-put
                        LOGGER.debug("read, unwrap result %s", result);
                        inputBuffer.compact(); // inputBuffer-compact
                        switch(result.getStatus()){
                            case BUFFER_OVERFLOW:
                                assert false: "read, buffer overflow";
                            case BUFFER_UNDERFLOW:
                                LOGGER.debug("read, underflow");
                                dispatcher.setChannelInterestNow(socketChannel, SelectionKey.OP_READ, true);
                                break;
                            case CLOSED:
                                LOGGER.debug("read, closed");
                                sslEngine.closeInbound();
                                done();
                                break;
                            case OK:
                                appInputBuffer.flip(); // appInputBuffer-flip
                                exchanger.handlePostIn(appInputBuffer); // appInputBuffer-get
                                appInputBuffer.compact(); // appInputBuffer-compact
                                exchanger.applyInterest();
                                break;
                            default:
                                assert false: result.getStatus();
                        }
                        break;
                    default:
                        assert false: sslEngine.getHandshakeStatus();
                }
            } else if (read < 0){
                done();
            } else{
                LOGGER.debug("read 0 bytes in read handler, unexpected condition...");
            }
        } catch(IOException x){
            LOGGER.error("cannot handle read", x);
            done();
        }
    }


    public void handleWrite() {
        try{
            if (outputBuffer.position() > 0){
                dispatcher.setChannelInterestNow(socketChannel, SelectionKey.OP_WRITE, true);
            } else{
                while (sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK || sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_WRAP){
                    switch(sslEngine.getHandshakeStatus()){
                        case NEED_TASK:
                            LOGGER.debug("write, running task(s)");
                            Runnable task;
                            long now = System.currentTimeMillis();
                            while ((task = sslEngine.getDelegatedTask()) != null) task.run();
                            LOGGER.info("task duration: %dms", System.currentTimeMillis() - now);
                            break;
                        case NEED_WRAP:
                            LOGGER.debug("write, wrap needed, writing more");
                            SSLEngineResult sslEngineResult = sslEngine.wrap(appOutputBuffer, outputBuffer); // outputBuffer-get, outputBufferNet-put
                            LOGGER.debug("write, wrap result: " + sslEngineResult);
                            switch(sslEngineResult.getStatus()){
                                case BUFFER_UNDERFLOW:
                                    assert false: "write, buffer underflow";
                                case BUFFER_OVERFLOW:
                                    assert false: "write, buffer overflow";
                                case CLOSED:
                                    LOGGER.debug("write, closed");
                                    sslEngine.closeOutbound();
                                    done();
                                    return;
                                case OK:
                                    outputBuffer.flip(); // outputBuffer-flip
                                    int written = socketChannel.write(outputBuffer); // outputBuffer-get
                                    LOGGER.debug("wrote bytes to socket channel: %d", written);
                                    outputBuffer.compact(); // outputBuffer-compact
                                    dispatcher.setChannelInterestNow(socketChannel, SelectionKey.OP_WRITE, true);
                                    return;
                                default:
                                    assert false: sslEngineResult.getStatus();
                            }
                        default:
                            assert false: sslEngine.getHandshakeStatus();
                    }
                }
                switch(sslEngine.getHandshakeStatus()){
                    case NEED_UNWRAP:
                        LOGGER.debug("write, unwrap needed, switching to reading");
                        dispatcher.setChannelInterestNow(socketChannel, SelectionKey.OP_READ, true);
                        break;
                    case FINISHED:
                        LOGGER.info("read, handshake completed, setting initial interest");
                        exchange();
                        break;
                    case NOT_HANDSHAKING:
                        exchanger.handlePreOut(appOutputBuffer); // appOutputBuffer-put
                        appOutputBuffer.flip(); // appOutputBuffer-flip
                        SSLEngineResult result = sslEngine.wrap(appOutputBuffer, outputBuffer); // appOutputBuffer-get, outputBuffer-put
                        LOGGER.debug("write, wrap result %s", result);
                        appOutputBuffer.compact(); // appOutputBuffer-compact
                        assert result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING: "ssl engine is handshaking";
                        switch(result.getStatus()){
                            case BUFFER_OVERFLOW:
                                assert false: "ssl engine write buffer overflow";
                            case BUFFER_UNDERFLOW:
                                assert false: "ssl engine write buffer underflow";
                            case CLOSED:
                                LOGGER.debug("write, ssl engine closed");
                                sslEngine.closeOutbound();
                                done();
                                break;
                            case OK:
                                outputBuffer.flip(); // outputBuffer-flip
                                int written = socketChannel.write(outputBuffer); // outputBuffer-get
                                LOGGER.debug("wrote bytes to socket channel: %d", written);
                                outputBuffer.compact(); // outputBuffer-compact
                                if (outputBuffer.position() > 0){
                                    dispatcher.setChannelInterestNow(socketChannel, SelectionKey.OP_WRITE, true);
                                } else{
                                    exchanger.applyInterest();
                                }
                                break;
                            default:
                                assert false: result.getStatus();
                        }
                        break;
                    default:
                        assert false: sslEngine.getHandshakeStatus();
                }
            }
        } catch(IOException x){
            LOGGER.error("cannot handle write", x);
            done();
        }
    }

}
