package samples;

import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Mar 28, 2006
 * Time: 12:25:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class BoundedSocket {
    public static void main(final String[] args) throws Exception {
        final InetAddress remoteInetAddress = InetAddress.getByName(args[0]);
        int remotePort = Integer.parseInt(args[1]);
        final InetAddress localInetAddress = InetAddress.getByName(args[2]);
        int localPort = Integer.parseInt(args[3]);
        System.out.printf("connecting: %s:%s to %s:%s%n", localInetAddress, localPort, remoteInetAddress, remotePort);
        final Socket socket = new Socket(remoteInetAddress, remotePort, localInetAddress, localPort);
        System.out.printf("ok%n");
    }
}
