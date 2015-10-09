package net.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 19, 2004
 * Time: 2:10:01 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Server implements SelectorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    protected Dispatcher dispatcher;
    protected ServerSocketChannel serverSocketChannel;

    public Server(Dispatcher dispatcher, ServerSocketChannel serverSocketChannel, int port) throws IOException {
        this.dispatcher = dispatcher;
        this.serverSocketChannel = serverSocketChannel;
        serverSocketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        LOGGER.debug("binding server socket channel to port: " + port);
        if (!serverSocketChannel.isOpen()) throw new IOException("cannot bind closed server socket channel");
        serverSocketChannel.socket().bind(inetSocketAddress);
        LOGGER.debug("server socket channel bound to port: " + port);
    }

    public void open() throws IOException {
        LOGGER.debug("registering server socket channel for 'accept'");
        dispatcher.registerChannelLater(serverSocketChannel, SelectionKey.OP_ACCEPT ,this, new ErrorHandler(){
            public void handleError(Exception x){
                LOGGER.error("error registering server for 'accept'", x);
            }
        });
    }

    public boolean isOpen() {
        return serverSocketChannel != null && serverSocketChannel.isOpen();
    }

    public void close() throws IOException {
        if (isOpen()) try{
            serverSocketChannel.close();
        } finally{
            serverSocketChannel = null;
        }
    }

    public abstract void socketConnected(Dispatcher dispatcher, SocketChannel socketChannel) throws IOException;

    public void handleAccept() {
        SocketChannel socketChannel = null;
        try{
            socketChannel = serverSocketChannel.accept();
            LOGGER.debug(String.format("client accepted, local: %s, remote: %s", socketChannel.socket().getLocalSocketAddress(), socketChannel.socket().getRemoteSocketAddress()));
            dispatcher.registerChannelNow(serverSocketChannel, SelectionKey.OP_ACCEPT, this);
            socketConnected(dispatcher, socketChannel);
        } catch(IOException x){
            LOGGER.error("cannot accept connection request", x);
            if (socketChannel != null && socketChannel.isOpen()) try{
                socketChannel.close();
            } catch(IOException x2){
                LOGGER.error("cannot close socket channel", x2);
            }
        }
    }

    public void handleConnect() {
    }

    public void handleRead() {
    }

    public void handleWrite() {
    }
}
