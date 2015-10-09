package parser.pdf.impl;

import java.io.IOException;
import java.io.InputStream;

public class BufferedPushbackInputStream extends InputStream {
    private final InputStream in;
    private final byte[] buf;
    private int limit = 0;
    private int position = 0;
    private int countIn = 0;
    private boolean marked = false;
    private byte[] value;
    private int count;
    private boolean eof = false;

    public BufferedPushbackInputStream(final InputStream in, int bufferSize) {
        this.in = in;
        this.buf = new byte[bufferSize];
        this.value = new byte[bufferSize]; // grows if needed
    }

    public int getPosition(){
        return countIn;
    }

    public byte[] getValue(){
        final byte[] v = new byte[count];
        System.arraycopy(value, 0, v, 0, count);
        return v;
    }

    private void push(int b){
        if (count == value.length){
            int length = ((value.length / 2) + 1) * 3;
            byte[] bytes = new byte[length];
            System.arraycopy(value, 0, bytes, 0, value.length);
            value = bytes;
        }
        value[count] = (byte) b;
        count++;
    }

    public int read() throws IOException {
        if (eof) return -1;
        if (position == limit){
            int read = in.read(buf);
//            System.out.printf("read: %s bytes%n", read);
            eof = read < 0;
            if (eof) return -1;
            limit = read;
            position = 0;
            marked = false;
        }
        int b = buf[position];
        push(b);
        position++;
        countIn++;
        return b & 0xFF;
    }

    public void mark() throws IOException {
        int remaining = limit - position;
        System.arraycopy(buf, position, buf, 0, remaining);
        int read = in.read(buf, remaining, buf.length - remaining);
//        System.out.printf("read: %s bytes%n", read);
        limit = read < 0 ? remaining : remaining + read;
        position = 0;
        count = 0;
        marked = true;
    }

    public void reset() throws IOException {
        if (!marked) throw new IOException("pushback exceeds buffer size");
        countIn -= position;
        position = 0;
        count = 0;
        eof = false;
    }

    public void back() throws IOException {
        if (position == 0) throw new IOException("back: pushback underflow");
        position--;
        countIn--;
        count--;
        eof = false;
    }

    public void escape(int length, int b) throws IOException {
        if (length > count) throw new IOException("escape: pushback underflow");
        count -= length;
        if (b != 0) push(b);
    }

    public void escape(int b) throws IOException {
        escape(2, b);
    }
}
