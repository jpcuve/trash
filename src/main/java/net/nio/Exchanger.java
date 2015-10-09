package net.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 31, 2004
 * Time: 10:57:27 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Exchanger implements SelectorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Exchanger.class);
    protected Dispatcher dispatcher;
    protected SocketChannel socketChannel;
    private Interest interest;
    private ByteBuffer inputBuffer;
    private ByteBuffer outputBuffer;

    public enum Interest{
        NONE, READ, WRITE, CLOSE
    }

    public Exchanger(Dispatcher dispatcher, SocketChannel socketChannel, Interest interest) throws IOException {
        this.dispatcher = dispatcher;
        this.socketChannel = socketChannel;
        this.interest = interest;
        // this.socketChannelPort = socketChannel.socket().getRemoteSocketAddress().toString() + '-' + Integer.toString(socketChannel.socket().getLocalPort()).toUpperCase();
        this.inputBuffer = ByteBuffer.allocateDirect(socketChannel.socket().getReceiveBufferSize());
        this.outputBuffer = ByteBuffer.allocateDirect(socketChannel.socket().getSendBufferSize());
        socketChannel.configureBlocking(false);
    }

    public abstract void handlePostIn(ByteBuffer inputBuffer) throws IOException;
    public abstract void handlePreOut(ByteBuffer outputBuffer) throws IOException;

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public Interest getInterest() {
        return interest;
    }

    public void setInterest(Interest interest){
        this.interest = interest;
    }

    public void applyInterest(Interest interest) {
        this.interest = interest;
        try{
            applyInterest();
        } catch(IOException x){
            LOGGER.error("cannot apply interest %s, terminating", interest, x);
            done();
        }
    }

    public void applyInterest() throws IOException {
        switch(interest){
            case NONE:
                break;
            case READ:
                if (Thread.currentThread() == dispatcher.getThread()) dispatcher.setChannelInterestNow(socketChannel, SelectionKey.OP_READ, true);
                else dispatcher.setChannelInterestLater(socketChannel, SelectionKey.OP_READ, true, new ErrorHandler(){
                    public void handleError(Exception x) {
                        LOGGER.error("cannot set later 'read' interest, terminating", x);
                        done();
                    }
                });
                break;
            case WRITE:
                if (Thread.currentThread() == dispatcher.getThread()) dispatcher.setChannelInterestNow(socketChannel, SelectionKey.OP_WRITE, true);
                else dispatcher.setChannelInterestLater(socketChannel, SelectionKey.OP_WRITE, true, new ErrorHandler(){
                    public void handleError(Exception x) {
                        LOGGER.error("cannot set later 'write' interest, terminating", x);
                        done();
                    }
                });
                break;
            case CLOSE:
                done();
                break;
            default:
                assert false;
        }
    }

    public void open() throws IOException {
        LOGGER.debug("connected socket channel: " + socketChannel.socket().getLocalSocketAddress() + " " + socketChannel.socket().getRemoteSocketAddress());
        switch(interest){
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
                assert false: interest;
        }
    }

    public void close() {
        setInterest(Interest.CLOSE);
    }

    public boolean isOpen() {
        return (socketChannel != null && socketChannel.isOpen());
    }

    protected void done() {
        if (socketChannel != null) try {
            LOGGER.debug("closing socket channel : " + socketChannel.socket().getLocalSocketAddress() + " " + socketChannel.socket().getRemoteSocketAddress());
            socketChannel.close();
        } catch(IOException x){
            LOGGER.error("cannot close socket channel", x);
        }
    }

    public void handleRead() {
        try{
            int read = socketChannel.read(inputBuffer); // inputBuffer-put
            LOGGER.debug("read bytes from socket channel: %d", read);
            assert read != 0: "read 0 bytes from network on 'read' event";
            if (read > 0){
                inputBuffer.flip(); // inputBuffer-flip
                handlePostIn(inputBuffer); // inputBuffer-get
                inputBuffer.compact(); // inputBuffer-compact
                applyInterest();
            } else{
                done();
            }
        } catch(IOException x){
            LOGGER.error("cannot handle read", x);
            done();
        }
    }

    public void handleWrite() {
        try{
            handlePreOut(outputBuffer); // outputBuffer-put
            outputBuffer.flip(); // outputBuffer-flip
            int written = socketChannel.write(outputBuffer); // outputBuffer-get
            LOGGER.debug("wrote bytes to socket channel: %d", written);
            outputBuffer.compact(); // outputBuffer-compact
            if (outputBuffer.position() > 0){
                dispatcher.setChannelInterestNow(socketChannel, SelectionKey.OP_WRITE, true);
            } else{
                applyInterest();
            }
        } catch(IOException x){
            LOGGER.error("cannot handle write", x);
            done();
        }
    }

}
