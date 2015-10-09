package parser.pdf.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class RandomAccessFileInputStream extends InputStream {
    private final RandomAccessFile randomAccessFile;
    private long start;
    private long length;
    private long countIn = 0;

    public RandomAccessFileInputStream(final File file, final long start) throws IOException {
        this(file, start, -1);
    }

    public RandomAccessFileInputStream(final File file, final long start, final long length) throws IOException {
        this.randomAccessFile = new RandomAccessFile(file, "r");
        adjust(start, length);
    }

    public void adjust(long start, long length) throws IOException {
        this.start = start;
        this.length = length;
        this.countIn = 0;
        randomAccessFile.seek(start);
    }

    public long getLength() {
        return length;
    }

    public long getStart() {
        return start;
    }

    public int read() throws IOException {
        if (length > 0 && countIn >= length) return -1;
        int b = randomAccessFile.read();
        if (b != -1) countIn++;
        return b;
    }

    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte b[], int off, int len) throws IOException {
        if (length > 0 && countIn >= length) return -1;
        if (length > 0 && len > length - countIn) len = (int) (length - countIn);
        int read = randomAccessFile.read(b, off, len);
        if (read != -1) countIn += read;
        return read;
    }
}
