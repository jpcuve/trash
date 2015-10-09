package net.nio.protocol.line;

import net.nio.Dispatcher;
import net.nio.Server;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 14, 2005
 * Time: 5:01:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class LineServer extends Server {
    public LineServer(Dispatcher dispatcher, int port) throws IOException {
        super(dispatcher, ServerSocketChannel.open(), port);
    }

    public void pulse() {
    }

    public void socketConnected(Dispatcher dispatcher, SocketChannel socketChannel) throws IOException {
        LineWorker lineWorker = new LineWorker(dispatcher, socketChannel);
        lineWorker.open();
    }

    public static void main(String[] args) throws IOException {
        Dispatcher dispatcher = new Dispatcher(200);
        new Thread(dispatcher).start();
        LineServer lineServer = new LineServer(dispatcher, 9999);
        lineServer.open();
    }
}
