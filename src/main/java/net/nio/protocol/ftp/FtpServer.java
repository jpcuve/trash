package net.nio.protocol.ftp;

import net.nio.Dispatcher;
import net.nio.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 3, 2004
 * Time: 9:20:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class FtpServer extends Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpServer.class);
    private List<FtpWorker> ftpWorkers = new ArrayList<FtpWorker>();
    private int maxConnections;
    private InetAddress[] inetAddresses;

    public FtpServer(Dispatcher dispatcher, int port, int maxConnections, InetAddress[] inetAddresses) throws IOException {
        super(dispatcher, ServerSocketChannel.open(), port);
        this.maxConnections = maxConnections;
        this.inetAddresses = inetAddresses;
    }

    public void pulse() {
    }

    public void socketConnected(Dispatcher dispatcher, SocketChannel socketChannel) throws IOException {
        List<FtpWorker> deads = new ArrayList<FtpWorker>();
        for (FtpWorker ftpWorker: ftpWorkers) if (ftpWorker.getSocketChannel().socket().isClosed()) deads.add(ftpWorker);
        ftpWorkers.removeAll(deads);
        InetAddress inetAddress = ((InetSocketAddress)socketChannel.socket().getRemoteSocketAddress()).getAddress();
        boolean ip = true;
        if (inetAddresses != null && !InetAddress.getLocalHost().equals(inetAddress)){
            LOGGER.debug("verifying if client ip address is allowed");
            ip = false;
            for (InetAddress allowed: inetAddresses) if (allowed.equals(inetAddress)) ip = true;
        }
        LOGGER.debug("total current live connections: " + ftpWorkers.size());
        int count = 0;
        LOGGER.debug("probing remote host: " + inetAddress);
        for (FtpWorker ftpWorker: ftpWorkers){
            InetSocketAddress inetSocketAddress = (InetSocketAddress)ftpWorker.getSocketChannel().socket().getRemoteSocketAddress();
            if (inetSocketAddress != null && inetAddress.equals(inetSocketAddress.getAddress())) count++;
        }
        LOGGER.debug("current live connection for the remote host: " + count);
        if (count < maxConnections && ip){
            FtpWorker ftpWorker = new FtpWorker(dispatcher, socketChannel, 40000);
            ftpWorkers.add(ftpWorker);
            ftpWorker.open();
        }
        else socketChannel.close();
    }

    public static void main(String[] args) throws Exception {
        Dispatcher dispatcher = new Dispatcher(100);
        new Thread(dispatcher).start();
        FtpServer ftpServer = new FtpServer(dispatcher, 21, 2, null);
        ftpServer.open();
    }
}
