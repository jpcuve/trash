package parser.pdf.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class LineReader {
    private final FileChannel fileChannel;
    private final String charsetName;
    private final byte[] data;
    private final ByteBuffer buffer;
    private int lineByteCount = 0;
    private int totalByteCount = 0;

    public LineReader(final File file, String charsetName, final int maxLineByteSize) throws IOException {
        this.fileChannel = new RandomAccessFile(file, "r").getChannel();
        this.charsetName = charsetName;
        this.data = new byte[maxLineByteSize];
        this.buffer = ByteBuffer.wrap(data);
    }

    public void reposition(final long position) throws IOException {
        buffer.clear();
        lineByteCount = totalByteCount = 0;
        fileChannel.position(position);
    }

    public String readLine() throws IOException {
        this.lineByteCount = 0;
        byte b;
        fileChannel.read(buffer);
        buffer.flip();
        if (buffer.remaining() <= 0) return null;
        do {
            b = buffer.get();
            this.lineByteCount++;
        } while (b != 10 && b != 13 && buffer.remaining() > 0);
        int stringLength = lineByteCount - 1;
        if (b == 13 && buffer.remaining() > 0 && data[buffer.position()] == 10){
            buffer.get();
            this.lineByteCount++;
        }
        totalByteCount += lineByteCount;
        final String ret = new String(data, 0, stringLength, charsetName);
        buffer.compact();
        return ret; 
    }

    public int getLastLineByteCount(){ // EOL included
        return lineByteCount;
    }

    public int getTotalByteCount(){ // EOL included
        return totalByteCount;
    }
}
