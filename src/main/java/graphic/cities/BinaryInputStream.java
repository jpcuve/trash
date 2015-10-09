package graphic.cities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 3, 2010
 * Time: 1:52:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryInputStream extends InputStream {
    private static final Logger LOG = LoggerFactory.getLogger(BinaryInputStream.class);
    private final InputStream is;

    public BinaryInputStream(final InputStream is) {
        this.is = is;
    }

    public long readLong(int byteCount, boolean littleEndian) throws IOException {
        final byte[] ar = new byte[byteCount];
        if (is.read(ar) == -1) throw new EOFException();
        long l = 0;
        for (int i = 0; i < ar.length; i++) l |= (long) (ar[i] < 0 ? 256 + ar[i] : ar[i]) << ((littleEndian ? i : ar.length - 1 - i) * 8);
        return l;
    }

    @Override
    public int read() throws IOException {
        return is.read();
    }

    @Override
    public long skip(long l) throws IOException {
        return is.skip(l);
    }

    @Override
    public int available() throws IOException {
        return is.available();
    }

    @Override
    public void close() throws IOException {
        is.close();
    }

    @Override
    public void mark(int i) {
        is.mark(i);
    }

    @Override
    public void reset() throws IOException {
        is.reset();
    }

    @Override
    public boolean markSupported() {
        return is.markSupported();
    }
}
