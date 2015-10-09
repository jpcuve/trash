package net.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 29, 2004
 * Time: 2:16:10 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Client<Q, R> extends Exchanger {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    protected Client(final Dispatcher dispatcher, SocketChannel socketChannel, Interest interest, InetSocketAddress inetSocketAddress) throws IOException {
        super(dispatcher, socketChannel, interest);
        LOGGER.debug("starting connection to: " + inetSocketAddress);
        socketChannel.connect(inetSocketAddress);
    }

    public abstract void init(R r) throws IOException;
    public abstract void query(Q q, R r) throws IOException;

    public void open() throws IOException {
        LOGGER.debug("registering socket channel for 'connect'");
        dispatcher.registerChannelLater(socketChannel, SelectionKey.OP_CONNECT ,this, new ErrorHandler(){
            public void handleError(Exception x){
                LOGGER.error("error registering client for 'connect'", x);
            }
        });
    }

    public void handleAccept() {
    }

    public void handleConnect() {
        try{
            if (socketChannel.isConnectionPending()){
                LOGGER.debug("finishing connection");
                if (!socketChannel.finishConnect()) throw new IOException("cannot finish connection");
            }
            LOGGER.debug(String.format("client connected, local: %s, remote: %s", socketChannel.socket().getLocalSocketAddress(), socketChannel.socket().getRemoteSocketAddress()));
            applyInterest();
        } catch(IOException x){
            LOGGER.error("cannot finish connection", x);
            done();
        }
    }

}
