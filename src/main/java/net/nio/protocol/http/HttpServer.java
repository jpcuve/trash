package net.nio.protocol.http;

import net.nio.Dispatcher;
import net.nio.Server;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 25, 2005
 * Time: 4:21:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpServer extends Server {
    public HttpServer(Dispatcher dispatcher, int port) throws IOException {
        super(dispatcher, ServerSocketChannel.open(), port);
    }

    public void pulse() {
    }

    public void socketConnected(Dispatcher dispatcher, SocketChannel socketChannel) throws IOException {
        HttpWorker httpWorker = new HttpWorker(dispatcher, socketChannel);
        httpWorker.open();
    }

    public static void main(String[] args) throws IOException {
        Dispatcher dispatcher = new Dispatcher(200);
        Thread thread = new Thread(dispatcher);
        thread.setName("dispatcher-" + thread.getId());
        thread.start();
        HttpServer httpServer = new HttpServer(dispatcher, 8080);
        httpServer.open();
    }
}
