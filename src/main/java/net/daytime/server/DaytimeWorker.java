package net.daytime.server;

import net.nio.Dispatcher;
import net.nio.Worker;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 6, 2006
 * Time: 4:00:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class DaytimeWorker extends Worker<Object, DaytimeResponse> implements DaytimeHandler {
    private DaytimeResponse response = new DaytimeResponse(this);

    public DaytimeWorker(Dispatcher dispatcher, SocketChannel socketChannel) throws IOException {
        super(dispatcher, socketChannel, Interest.WRITE);
    }

    public void service(Object object, DaytimeResponse daytimeResponse) {
    }

    public void handlePostIn(ByteBuffer inputBuffer) throws IOException {
    }

    public void handlePreOut(ByteBuffer outputBuffer) throws IOException {
        response.writeTo(outputBuffer);
    }

    public void pulse() {
    }

    public void responseWritten(){
        response.reset();
        setInterest(Interest.CLOSE);
    }
}
