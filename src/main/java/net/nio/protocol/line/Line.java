package net.nio.protocol.line;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 24, 2005
 * Time: 4:04:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Line {
    public static final Logger LOGGER = LoggerFactory.getLogger(Line.class);
    private LineHandler lineHandler;
    private static final int MAX_LENGTH = 32;
    private byte[] buffer = "\r\n".getBytes();
    private int index = 0;
    private String line;

    public Line(LineHandler lineHandler){
        this.lineHandler = lineHandler;
        setLine("empty line to stop");
    }

    public Line(LineHandler lineHandler, String line) {
        this.lineHandler = lineHandler;
        setLine(line);
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
        this.buffer = (line + "\r\n").getBytes();
        index = 0;
    }

    public void reset(){
        index = 0;
    }

    public void readFrom(ByteBuffer inputBuffer) throws IOException {
        while (inputBuffer.hasRemaining()){
            byte b = inputBuffer.get();
            if (index == MAX_LENGTH) throw new IOException("maximum size reached");
            buffer[index] = b;
            if (b == 10){
                int l = index--;
                if (l > 0 && buffer[l - 1] == 13) l--;
                this.line = new String(buffer, 0, l);
                lineHandler.lineRead(line);
            }
            index++;
        }
    }

    public void writeTo(ByteBuffer outputBuffer) {
        while (outputBuffer.hasRemaining() && index < buffer.length) outputBuffer.put(buffer[index++]);
        if (index == buffer.length) lineHandler.lineWritten(line);
    }
}
