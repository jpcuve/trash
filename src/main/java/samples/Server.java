/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Feb 2, 2004
 * Time: 10:00:48 AM
 * To change this template use Options | File Templates.
 */
package samples;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    public static void main(String[] args) throws IOException {
        if (args.length == 0){
            System.out.println("usage:");
            System.out.println(" <address> <port>, or");
            System.out.println(" <port> (binds to localhost)");
        }
        System.out.println("args[0]=" + args[0]);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        InetSocketAddress isa = args.length > 1 ? new InetSocketAddress(args[0], Integer.parseInt(args[1])) : new InetSocketAddress(Integer.parseInt(args[0]));
        ssc.socket().bind(isa);
        ssc.socket().setReuseAddress(true);
        ssc.configureBlocking(true);
        System.out.println("listening on " + isa);
        SocketChannel sc = ssc.accept();
        System.out.println("connection established with " + sc.socket().getInetAddress() + " " + sc.socket().getPort());
        ByteBuffer byteBuffer = ByteBuffer.wrap("ok, connected, closing...\r\n".getBytes());
        sc.write(byteBuffer);
        sc.close();
        ssc.close();
        System.out.println("done");
    }
}
