package net.nio;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public abstract class Worker<Q, R> extends Exchanger {
    protected Worker(Dispatcher dispatcher, SocketChannel socketChannel, Interest interest) throws IOException {
        super(dispatcher, socketChannel, interest);
    }

    public void handleConnect() {
    }

    public void handleAccept() {
    }

    public abstract void service(Q q, R r);
}
