package net.nio.protocol.http;

import net.nio.Dispatcher;
import net.nio.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 25, 2005
 * Time: 4:21:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpWorker extends Worker<HttpRequest, HttpResponse> implements HttpHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpWorker.class);
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private HttpRequest httpRequest = new HttpRequest(this);
    private HttpResponse httpResponse = new HttpResponse(this);

    public HttpWorker(Dispatcher dispatcher, SocketChannel socketChannel) throws IOException {
        super(dispatcher, socketChannel, Interest.READ);
    }

    public void lineRead(String line) {
        LOGGER.debug("line read: {}", line);
        LOGGER.debug(" method: {}, path: {}, query: {}, major: {}, minor: {}", new Object[]{httpRequest.getHttpMethod(), httpRequest.getPath(), httpRequest.getQuery(), httpRequest.getVersionMajor(), httpRequest.getVersionMinor()});
    }

    public void headerRead(HttpHeader header) {
        LOGGER.debug("header read: %s", header);
    }

    public void headersRead() {
        LOGGER.debug("headers read, content-length: %d, content-type: %s, close", httpRequest.getLength(), httpRequest.getType());
        httpResponse.reset();
        service(httpRequest, httpResponse);
        if (httpRequest.getLength() == 0) dataRead();
    }

    public void dataRead() {
        LOGGER.debug("data read");
        setInterest(Interest.WRITE);
    }

    public void lineWritten(String line) {
        LOGGER.debug("line written: %s", line);
    }

    public void headerWritten(HttpHeader header) {
        LOGGER.debug("header written: %s", header);
    }

    public void headersWritten() {
        LOGGER.debug("headers written, content-length: %d, content-type: %s, close", httpResponse.getLength(), httpResponse.getType());
        if (httpResponse.getLength() == 0) dataWritten();
    }

    public void dataWritten() {
        LOGGER.debug("data written");
        httpRequest.reset();
        setInterest(Interest.READ);
    }

    public void service(HttpRequest q, HttpResponse r) {
        LOGGER.debug("servicing");
        r.setStatus(200, "OK", q.getVersionMajor(), q.getVersionMinor());
        r.setTextResponse(new Date() + " This is some résponse", Charset.defaultCharset(), "text/plain");
    }

    public void handlePostIn(ByteBuffer inputBuffer) throws IOException {
        httpRequest.readFrom(inputBuffer);
    }

    public void handlePreOut(ByteBuffer outputBuffer) throws IOException {
        httpResponse.writeTo(outputBuffer);
    }

    public void pulse() {
    }
}
