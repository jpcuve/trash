package net.daytime.server;

import net.nio.Dispatcher;
import net.nio.Server;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 6, 2006
 * Time: 3:59:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DaytimeServer extends Server {
    public DaytimeServer(Dispatcher dispatcher) throws IOException {
        super(dispatcher, ServerSocketChannel.open(), 13);
    }

    public void socketConnected(Dispatcher dispatcher, SocketChannel socketChannel) throws IOException {
        DaytimeWorker daytimeWorker = new DaytimeWorker(dispatcher, socketChannel);
        daytimeWorker.open();
    }

    public void pulse() {
    }

    public static void main(String[] args) throws Exception {
        Dispatcher dispatcher = new Dispatcher(100);
        new Thread(dispatcher).start();
        DaytimeServer daytimeServer = new DaytimeServer(dispatcher);
        daytimeServer.open();
    }
}
