package net.daytime.server;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 6, 2006
 * Time: 4:01:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DaytimeResponse {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final Charset CHARSET_ASCII = Charset.forName("US-ASCII");
    private DaytimeHandler daytimeHandler;
    private ByteBuffer buffer;

    public DaytimeResponse(DaytimeHandler daytimeHandler) {
        this.daytimeHandler = daytimeHandler;
    }

    public void reset(){
        this.buffer = null;
    }

    public void writeTo(ByteBuffer outputBuffer) {
        if (buffer == null){
            buffer = CHARSET_ASCII.encode(CharBuffer.wrap(DATE_FORMAT.format(new Date())));
        }
        int l = Math.min(buffer.remaining(), outputBuffer.remaining());
        for (int i = 0; i < l; i++) outputBuffer.put(buffer.get());
        if (!buffer.hasRemaining()){
            daytimeHandler.responseWritten();
        }
    }
}
