/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 29, 2003
 * Time: 8:27:44 AM
 * To change this template use Options | File Templates.
 */
package samples;

import java.net.InetAddress;
import java.nio.channels.SocketChannel;

public class Hosts {
    public static void main(String[] args) throws Exception {
        InetAddress[] ias = InetAddress.getAllByName("localhost");
        for (int i = 0; i < ias.length; i++) System.out.println("ias[" + i + "]=" + ias[i]);
        System.out.println("localhost=" + InetAddress.getLocalHost());
        SocketChannel sc = SocketChannel.open();
        sc.socket().setReuseAddress(true);
        /*
        sc.socket().bind(new InetSocketAddress(localAddress, localPort));
        sc.connect(new InetSocketAddress(localAddress, remotePort));
        */


    }
}
