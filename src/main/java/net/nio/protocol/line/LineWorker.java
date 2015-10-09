package net.nio.protocol.line;

import net.nio.Dispatcher;
import net.nio.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 14, 2005
 * Time: 5:05:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class LineWorker extends Worker<Line, Line> implements LineHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(LineWorker.class);
    private Line request = new Line(this);
    private Line response = new Line(this);

    public LineWorker(Dispatcher dispatcher, SocketChannel socketChannel) throws IOException {
        super(dispatcher, socketChannel, Interest.WRITE);
    }

    public void pulse() {
    }

    public void lineRead(String line) {
        LOGGER.info("request: %s", request.getLine());
        service(request, response);
        setInterest(response.getLine().length() == 0 ? Interest.CLOSE : Interest.WRITE);
    }

    public void lineWritten(String line) {
        LOGGER.info("response: %s", response.getLine());
        request.reset();
        setInterest(Interest.READ);
    }

    public void handlePostIn(ByteBuffer inputBuffer) throws IOException {
        request.readFrom(inputBuffer);
    }

    public void handlePreOut(ByteBuffer outputBuffer) throws IOException {
        response.writeTo(outputBuffer);
    }

    public void service(Line request, Line response){
        LOGGER.info("request: %s", request.getLine());
        response.setLine(request.getLine());
        LOGGER.info("response: %s", response.getLine());
    }
}
