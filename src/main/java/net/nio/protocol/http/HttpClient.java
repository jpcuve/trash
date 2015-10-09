package net.nio.protocol.http;

import net.nio.Client;
import net.nio.Dispatcher;
import net.nio.MemoryBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 27, 2005
 * Time: 12:03:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpClient extends Client<HttpRequest, HttpResponse> implements HttpHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);
    private CountDownLatch latch;
    private HttpRequest request;
    private HttpResponse response;

    public HttpClient(Dispatcher dispatcher, InetSocketAddress inetSocketAddress) throws IOException {
        super(dispatcher, SocketChannel.open(), Interest.NONE, inetSocketAddress);
    }

    public void init(HttpResponse r) throws IOException {
        open();
    }

    public void pulse() {
    }

    public void query(HttpRequest q, HttpResponse r) throws IOException {
        this.latch = new CountDownLatch(1);
        request = q;
        response = r;
        applyInterest(Interest.WRITE);
        boolean ok = false;
        try{
            ok = latch.await(3000, TimeUnit.MILLISECONDS);
        } catch(InterruptedException x){
            LOGGER.error("interrupted", x);
        }
        if (!ok) throw new IOException("query timeout");
    }

    public void handlePostIn(ByteBuffer inputBuffer) throws IOException {
        response.readFrom(inputBuffer);
    }

    public void handlePreOut(ByteBuffer outputBuffer) throws IOException {
        request.writeTo(outputBuffer);
    }

    public void lineRead(String line) {
        LOGGER.debug("line read: %s", line);
        LOGGER.debug(" code: {} message: {}, major: {}, minor: {}", new Object[]{response.getCode(), response.getMessage(), response.getVersionMajor(), response.getVersionMinor()});
    }

    public void headerRead(HttpHeader header) {
        LOGGER.debug("header read: %s", header);
    }

    public void headersRead() {
        LOGGER.debug("headers read, content-length: %d, content-type: %s, close", response.getLength(), response.getType());
        if (response.getLength() == 0) dataRead();
    }

    public void dataRead() {
        LOGGER.debug("data read");
        setInterest(Interest.NONE);
        latch.countDown();
    }

    public void lineWritten(String line) {
        LOGGER.debug("line written: %s", line);
    }

    public void headerWritten(HttpHeader httpHeader) {
        LOGGER.debug("header written: %s:%s", httpHeader);
    }

    public void headersWritten() {
        LOGGER.debug("headers written, content-length: %d, content-type: %s, close", request.getLength(), response.getType());
        if (request.getLength() == 0) dataWritten();
    }

    public void dataWritten() {
        LOGGER.debug("data written");
        // response.reset();
        setInterest(Interest.READ);
    }

    public static void main(String[] args) throws Exception {
        Dispatcher dispatcher = new Dispatcher(200);
        Thread thread = new Thread(dispatcher);
        thread.setName("dispatcher-" + thread.getId());
        thread.start();
        try{
            HttpClient httpClient = new HttpClient(dispatcher, new InetSocketAddress(InetAddress.getByName("www.google.com"), 80));
            httpClient.init(null);
            HttpRequest httpRequest = new HttpRequest(httpClient, HttpMethod.GET, "/", null, 1, 1);
            HttpResponse httpResponse = new HttpResponse(httpClient);
            MemoryBuffer memoryBuffer = new MemoryBuffer(32768, 10);
            httpResponse.setWbc(memoryBuffer);
            httpClient.query(httpRequest, httpResponse);
            System.out.printf("http response, length: %d, type: %s%n", httpResponse.getLength(), httpResponse.getType());
            System.out.printf("%s%n", Charset.defaultCharset().decode(ByteBuffer.wrap(memoryBuffer.getBuffer())));
        } finally{
            dispatcher.kill();
        }
    }

}
