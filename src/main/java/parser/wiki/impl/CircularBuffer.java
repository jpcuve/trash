package parser.wiki.impl;

import java.io.IOException;
import java.io.Reader;

/**
 * @author jpc
 */
public class CircularBuffer<E> {
    public static final char EOT = '\u0004';
    private final Reader reader;
    private final int halfBufferSize;
    private final int bufferSize;

    private final StringBuilder sb;
    private final char[] buf;
    private int fwd;
    private int bck;
    private int skip;
    private E type;

    public CircularBuffer(final Reader reader, int halfBufferSize) {
        this.reader = reader;
        this.halfBufferSize = halfBufferSize;
        this.bufferSize = halfBufferSize * 2;
        buf = new char[bufferSize];
        fwd = bufferSize - 1;
        buf[fwd] = EOT;
        bck = fwd;
        skip = 0;
        sb = new StringBuilder(bufferSize);
    }

    public String getValue(){
        return sb.toString();
    }

    public E getType() {
        return type;
    }

    public char readChar() throws IOException {
        char c = buf[fwd % bufferSize];
        if (c == EOT && (fwd + 1) % halfBufferSize == 0) {
            fwd++;
            int j = (fwd / halfBufferSize) & 1;
            if (skip > 0) {
                assert skip == 1 : "character pushback length exceeds half buffer size";
                skip--;
            } else {
                int k;
                k = reader.read(buf, j * halfBufferSize, halfBufferSize - 1);
                int fl = (k != -1) ? (j * halfBufferSize + k) : (fwd % bufferSize);
                buf[fl] = EOT;
            }
            c = buf[fwd % bufferSize];
        }
        fwd++;
        sb.append(c);
        return c;
    }

    public boolean isDone(){
        return sb.length() > 0 && sb.charAt(0) == EOT; 
    }

    public void mark() {
        bck = fwd;
        sb.setLength(0);
    }

    public void reset(E e) {
        this.type = e;
        skip += (fwd / halfBufferSize - bck / halfBufferSize);
        fwd = bck;
        sb.setLength(0);
    }

    public void back() {
        fwd--;
        if (fwd % halfBufferSize == 0) {
            skip++;
            fwd--;
        }
        sb.setLength(sb.length() - 1);
    }
}
