package samples;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Sep 8, 2004
 * Time: 1:32:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestNonBlockingSSL {
    public static final String HOST = "www.paypal.com";
    public static final int PORT = 443;


    public static void main(String[] args) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, null, null);
        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(true);
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(HOST, PORT));
        long delay = 0;
        while (!socketChannel.finishConnect() && delay < 10000){
            Thread.sleep(delay);
            delay = delay * 3 / 2 + 1;
        }
        SSLSession sslSession = sslEngine.getSession();
        ByteBuffer empty = ByteBuffer.wrap(new byte[0]);
        ByteBuffer localNetData = ByteBuffer.allocate(sslSession.getPacketBufferSize());
        ByteBuffer localAppData = ByteBuffer.allocate(sslSession.getApplicationBufferSize());
        ByteBuffer remoteNetData = ByteBuffer.allocate(sslSession.getPacketBufferSize());
        ByteBuffer remoteAppData = ByteBuffer.allocate(sslSession.getApplicationBufferSize());

        localAppData.put("GET / HTTP/1.1\r\n\r\n".getBytes("US-ASCII"));
        localAppData.flip();

        sslEngine.beginHandshake();
        SSLEngineResult.HandshakeStatus handshakeStatus = sslEngine.getHandshakeStatus();
        while(localAppData.hasRemaining() || remoteNetData.hasRemaining()){
            switch(sslEngine.getHandshakeStatus()){
                case NEED_WRAP:
                    // System.out.println("\nneed wrap");
                    SSLEngineResult sslEngineResult = sslEngine.wrap(localAppData, localNetData);
                    handshakeStatus = sslEngineResult.getHandshakeStatus();
                    System.out.println("wrapped " + localNetData.position() + " bytes for network");
                    System.out.println("status=" + sslEngineResult.getStatus() + " " + sslEngine.getHandshakeStatus() + " produced=" + sslEngineResult.bytesProduced() + " consumed=" + sslEngineResult.bytesConsumed());
                    System.out.println("result handshake status=" + sslEngineResult.getHandshakeStatus());
                    localNetData.flip();
                    while (localNetData.hasRemaining()){
                        int written = socketChannel.write(localNetData);
                        System.out.println("wrote " + written + " bytes to network");
                        if (written == -1){
                            assert false: "socket closed for writing";
                        }
                    }
                    localNetData.compact();
                    break;
                case NEED_UNWRAP:
                    // System.out.println("\nneed unwrap");
                    int read = socketChannel.read(remoteNetData);
                    if (read != 0) System.out.println("read " + read + " bytes from network");
                    if (read == -1){
                        assert false: "socket closed for reading";
                    } else {
                        remoteNetData.flip();
                        sslEngineResult = sslEngine.unwrap(remoteNetData, remoteAppData);
                        handshakeStatus = sslEngineResult.getHandshakeStatus();
                        if (sslEngineResult.getStatus() == SSLEngineResult.Status.OK){
                            System.out.println("unwrapped " + remoteAppData.position() + " bytes for application");
                            System.out.println("status=" + sslEngineResult.getStatus() + " " + sslEngine.getHandshakeStatus() + " produced=" + sslEngineResult.bytesProduced() + " consumed=" + sslEngineResult.bytesConsumed());
                            System.out.println("result handshake status=" + sslEngineResult.getHandshakeStatus());
                        }
                        remoteNetData.compact();
                        assert !remoteAppData.hasRemaining(): "handshake cannot generate remote app data";
                    }
                    break;
                case NEED_TASK:
                    System.out.println("\nneed task");
                    sslEngine.getDelegatedTask().run();
                    handshakeStatus = sslEngine.getHandshakeStatus();
                    System.out.println("status=" + sslEngine.getHandshakeStatus());
                    break;
                case NOT_HANDSHAKING:
                    sslEngineResult = sslEngine.wrap(localAppData, localNetData);
                    handshakeStatus = sslEngineResult.getHandshakeStatus();
                    localNetData.flip();
                    while (localNetData.hasRemaining()){
                        int written = socketChannel.write(localNetData);
                        System.out.println("wrote " + written + " bytes to network");
                        if (written == -1){
                            assert false: "socket closed for writing";
                        }
                    }
                    localNetData.compact();
                    read = socketChannel.read(remoteNetData);
                    if (read != 0) System.out.println("read " + read + " bytes from network");
                    if (read == -1){
                        System.out.println("server closed connection");
                        remoteAppData.flip();
                        int length = remoteAppData.remaining();
                        for (int i = 0; i < length; i++){
                            byte b = remoteAppData.get();
                            System.out.print((char)b);
                        }
                        System.out.println("dump finished");
                        System.exit(0);
                        assert false: "socket closed for reading";
                    } else {
                        remoteNetData.flip();
                        sslEngineResult = sslEngine.unwrap(remoteNetData, remoteAppData);
                        handshakeStatus = sslEngineResult.getHandshakeStatus();
                        if (sslEngineResult.getStatus() == SSLEngineResult.Status.OK){
                            System.out.println("unwrapped " + remoteAppData.position() + " bytes for application");
                            System.out.println("status=" + sslEngineResult.getStatus() + " " + sslEngine.getHandshakeStatus() + " produced=" + sslEngineResult.bytesProduced() + " consumed=" + sslEngineResult.bytesConsumed());
                            System.out.println("result handshake status=" + sslEngineResult.getHandshakeStatus());
                        }
                        remoteNetData.compact();
                    }
                    break;
                default:
                    assert false: "unknown handshake status: " + sslEngine.getHandshakeStatus();
                    break;
            }
        }

        // performing communications




        // closing everything
        sslEngine.closeOutbound();
        while (!sslEngine.isOutboundDone()){
            SSLEngineResult sslEngineResult = sslEngine.wrap(empty, localNetData);
            while (localNetData.hasRemaining()){
                int written = socketChannel.write(localNetData);
                System.out.println("wrote " + written + " bytes to network");
                if (written == -1){
                    assert false: "socket closed for writing";
                }
                localNetData.compact();
            }
        }
        socketChannel.close();
    }
}
