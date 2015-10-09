package net.tftp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 9, 2006
 * Time: 11:14:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class TftpServer implements Runnable {
    private DatagramSocket datagramSocket;
    byte[] buffer = new byte[1024];

    public TftpServer(int port) throws IOException {
        this.datagramSocket = new DatagramSocket(port);
    }

    public void run() {
        try{
            DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
            while(datagramSocket != null){
                System.out.printf("listening...%n");
                datagramSocket.receive(datagramPacket);
                System.out.printf("received bytes: %d%n", datagramPacket.getLength());
                for (int i = 0; i < datagramPacket.getLength(); i++) System.out.printf("%s ", datagramPacket.getData()[i]);
                System.out.printf("%n");
                datagramPacket.setLength(buffer.length);
            }
        } catch(IOException x){
            x.printStackTrace();
        } finally{
            if (datagramSocket != null) datagramSocket.close();
        }
    }

    public static void main(String[] args) throws Exception {
        TftpServer server = new TftpServer(13579);
        new Thread(server).start();
    }
}
