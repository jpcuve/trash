package net.nio.protocol.http;

import net.nio.Sink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 25, 2005
 * Time: 4:20:43 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class HttpMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpMessage.class);
    private static final Charset CHARSET_ASCII = Charset.forName("US-ASCII");
    private static final Sink SINK = new Sink();
    private static final String EOL = "\r\n";
    private HttpHandler httpHandler;
    private ByteBuffer buffer = null;
    private State state = State.LINE;
    protected String line;
    private Map<String, HttpHeader> headers = new HashMap<String, HttpHeader>();
    private Iterator<Map.Entry<String, HttpHeader>> headerIterator;
    private Map.Entry<String, HttpHeader> currentHeader;
    private String type = "";
    private int length = 0;
    private boolean close = false;

    private int count;
    private WritableByteChannel wbc;
    private ReadableByteChannel rbc;

    private enum State{
        LINE, HEADER, DATA, DONE
    }

    public HttpMessage(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
        reset();
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setHeader(String key, String value){
        headers.put(key.toLowerCase(), new HttpHeader(key, value));
    }

    public HttpHeader getHeader(String key){
        return headers.get(key.toLowerCase());
    }

    public Collection<HttpHeader> getHeaders(){
        return headers.values();
    }

    public WritableByteChannel getWbc() {
        return wbc;
    }

    public void setWbc(WritableByteChannel wbc) {
        this.wbc = wbc;
    }

    public ReadableByteChannel getRbc() {
        return rbc;
    }

    public void setRbc(ReadableByteChannel rbc) {
        this.rbc = rbc;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = this.count = length;
        setHeader("Content-Length", Integer.toString(length));
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        setHeader("Content-Type", type);
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public void reset(){
        buffer = null;
        state = State.LINE;
        line = "";
        headers.clear();
        rbc = SINK;
        wbc = SINK;
        length = 0;
        type = "";
        close = true;
    }

    public abstract void decodeLine() throws Exception;
    public abstract void encodeLine() throws Exception;

    public void readFrom(ByteBuffer inputBuffer) throws IOException {
        try{
            while (inputBuffer.hasRemaining()){
                byte b = inputBuffer.get();
                switch(state){
                    case LINE:
                        if (buffer == null) buffer = ByteBuffer.allocate(32768);
                        buffer.put(b);
                        if (b == 0x0A){
                            buffer.position(buffer.position() - 2);
                            buffer.flip();
                            this.line = CHARSET_ASCII.decode(buffer).toString();
                            decodeLine();
                            httpHandler.lineRead(line);
                            buffer.clear();
                            state = State.HEADER;
                        }
                        break;
                    case HEADER:
                        buffer.put(b);
                        if (b == 0x0A){
                            buffer.position(buffer.position() - 2);
                            buffer.flip();
                            String line = CHARSET_ASCII.decode(buffer).toString();
                            HttpHeader header;
                            if (line.length() != 0){
                                header = HttpHeader.decode(line);
                                headers.put(header.getKey().toLowerCase(), header);
                                httpHandler.headerRead(header);
                            } else{
                                if ((header = headers.get("content-length")) != null) try{
                                    count = length = Integer.parseInt(header.getValue());
                                } catch(NumberFormatException x){
                                    count = length = 0;
                                }
                                if ((header = headers.get("content-type")) != null) type = header.getValue();
                                httpHandler.headersRead();
                                if (length > 0) state = State.DATA;
                            }
                            buffer.clear();
                        }
                        break;
                    case DATA:
                        buffer.put(b); // byteBuffer-put
                        if (wbc != null){
                            buffer.flip(); // byteBuffer-flip
                            count -= wbc.write(buffer); // byteBuffer-get
                        } else count--;
                        buffer.compact(); // byteBuffer-compact
                        if (count <= 0){
                            state = State.LINE;
                            // LOGGER.debug("http request data channel closed");
                            wbc.close();
                            httpHandler.dataRead();
                            // reset();
                        }
                        break;
                }
            }
        } catch(Exception x){
            LOGGER.error("cannot read http message", x);
            throw new IOException(x.getMessage());
        }
    }

    public void writeTo(ByteBuffer outputBuffer) throws IOException {
        try{
            switch(state){
                case LINE:
                    if (buffer == null){
                        encodeLine();
                        buffer = CHARSET_ASCII.encode(CharBuffer.wrap(line + EOL));
                    }
                    int l = Math.min(buffer.remaining(), outputBuffer.remaining());
                    for (int i = 0; i < l; i++) outputBuffer.put(buffer.get());
                    if (!buffer.hasRemaining()){
                        httpHandler.lineWritten(line);
                        headerIterator = headers.entrySet().iterator();
                        state = State.HEADER;
                        buffer = null;
                    }
                    break;
                case HEADER:
                    if (buffer == null){
                        if (headerIterator.hasNext()){
                            currentHeader = headerIterator.next();
                            buffer = CHARSET_ASCII.encode(CharBuffer.wrap(HttpHeader.encode(currentHeader.getValue()) + EOL));
                        } else{
                            currentHeader = null;
                            buffer = CHARSET_ASCII.encode(CharBuffer.wrap(EOL));
                        }
                    }
                    l = Math.min(buffer.remaining(), outputBuffer.remaining());
                    for (int i = 0; i < l; i++) outputBuffer.put(buffer.get());
                    if (!buffer.hasRemaining()){
                        if (currentHeader != null){
                            httpHandler.headerWritten(currentHeader.getValue());
                            buffer = null;
                        } else{
                            httpHandler.headersWritten();
                            if (length > 0) state = State.DATA;
                        }
                    }
                    break;
                case DATA:
                    if (count > 0){
                        int read = rbc.read(outputBuffer);
                        if (read < 0) throw new IOException("unexpected connection close");
                        count -= read;
                    } else{
                        rbc.close();
                        httpHandler.dataWritten();
                    }
                    break;
            }
        } catch(Exception x){
            LOGGER.error("cannot write http message", x);
            throw new IOException(x.getMessage());
        }
    }
}
