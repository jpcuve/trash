package net.nio.protocol.line;

import net.nio.Client;
import net.nio.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 17, 2005
 * Time: 10:56:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class LineClient extends Client<Line, Line> implements LineHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LineClient.class);
    private CountDownLatch latch;
    private Line request;
    private Line response;

    public LineClient(final Dispatcher dispatcher, InetSocketAddress inetSocketAddress) throws IOException {
        super(dispatcher, SocketChannel.open(), Interest.READ, inetSocketAddress);
    }

    public void pulse() {
        System.out.printf("pulse%n");
    }

    public void init(Line r) throws IOException {
        this.latch = new CountDownLatch(1);
        response = r;
        r.reset();
        open();
        boolean ok = false;
        try{
            ok = latch.await(2000, TimeUnit.MILLISECONDS);
        } catch(InterruptedException x){
            LOGGER.error("interrupted", x);
        }
        if (!ok) throw new IOException("connection timeout");
    }

    public void query(Line q, Line r) throws IOException {
        this.latch = new CountDownLatch(1);
        request = q;
        response = r;
        r.reset();
        applyInterest(Interest.WRITE);
        boolean ok = false;
        try{
            ok = latch.await(3000, TimeUnit.MILLISECONDS);
        } catch(InterruptedException x){
            LOGGER.error("interrupted", x);
        }
        if (!ok) throw new IOException("query timeout");
    }

    public void lineRead(String line) {
        latch.countDown();
        setInterest(Interest.NONE);
    }

    public void lineWritten(String line) {
        setInterest(Interest.READ);
    }

    public void handlePostIn(ByteBuffer inputBuffer) throws IOException {
        response.readFrom(inputBuffer);
    }

    public void handlePreOut(ByteBuffer outputBuffer) throws IOException {
        request.writeTo(outputBuffer);
    }
}
