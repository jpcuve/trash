package parser.pdf.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class PdfInputStream extends InputStream {
    private final FileChannel fileChannel;
    private final long start;
    private final long length;
    private long read;
    private final ByteBuffer buffer = ByteBuffer.allocate(64 * 1024);

    public PdfInputStream(FileChannel fileChannel, long start, long length) throws IOException {
        this.fileChannel = fileChannel;
        this.start = start;
        this.length = length;
        this.read = 0;
        fileChannel.position(start);
        fileChannel.read(buffer);
        buffer.flip();
    }

    public long getStart() {
        return start;
    }

    public long getLength() {
        return length;
    }

    public int read() throws IOException {
        if (read == length) return -1;
        int b = buffer.get();
        read++;
        if (buffer.remaining() == 0){
            buffer.clear();
            fileChannel.read(buffer);
            buffer.flip();
        }
        return b;
    }
}
