package net.tftp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 9, 2006
 * Time: 12:26:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class TftpClient {
    private DatagramSocket datagramSocket;

    public TftpClient() throws IOException {
        this.datagramSocket = new DatagramSocket();
    }

    public void send(InetSocketAddress address, byte[] data) throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(data, 0, data.length, address);
        datagramSocket.send(datagramPacket);
    }

    public static void main(String[] args) throws Exception {
        TftpClient client = new TftpClient();
        InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), 13579);
        client.send(address, new byte[]{ 1, 2, 3, 4 });
    }
}
