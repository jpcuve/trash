package net.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 20, 2004
 * Time: 6:20:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class MemoryBuffer implements ByteChannel {
    private int maxBlockCount;
    private byte[] buffer;
    private boolean open = true;
    private int rPos = 0;
    private int wPos = 0;
    private List<byte[]> buffers = new ArrayList<byte[]>();

    public MemoryBuffer(int blockSize, int maxBlockCount) {
        this.maxBlockCount = maxBlockCount;
        this.buffer = new byte[blockSize];
    }

    public MemoryBuffer(int blockSize){
        this.maxBlockCount = 1;
        this.buffer = new byte[blockSize];
    }

    public MemoryBuffer(byte[] bytes){
        this.maxBlockCount = 1;
        this.buffer = bytes;
    }

    public MemoryBuffer(ByteBuffer byteBuffer){
        this.maxBlockCount = 1;
        this.buffer = new byte[byteBuffer.limit()];
        byteBuffer.get(buffer);
    }

    public void open(){
        rPos = 0;
        wPos = 0;
        buffers.clear();
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int read(ByteBuffer byteBuffer) throws IOException {
        if (rPos == buffer.length) return -1;
        int n = Math.min(byteBuffer.remaining(), buffer.length - rPos);
        byteBuffer.put(buffer, rPos, n);
        rPos += n;
        return n;
    }

    public boolean isOpen() {
        return open;
    }

    public void close() throws IOException {
        int l = buffer.length;
        byte[] data = new byte[buffers.size() * l + wPos];
        int pos = 0;
        for (byte[] bytes: buffers){
            System.arraycopy(bytes, 0, data, pos, l);
            pos += l;
        }
        System.arraycopy(buffer, 0, data, pos, wPos);
        buffer = data;
        open = false;
    }

    public void put(byte b){
        buffer[wPos] = b;
        wPos++;
        if (wPos == buffer.length){
            buffers.add(buffer);
            buffer = new byte[buffer.length];
            wPos = 0;
        }
    }

    public int write(ByteBuffer byteBuffer) throws IOException { // put bytes to channel; will always work...
        int qty = byteBuffer.remaining();
        int total = qty;
        int n = Math.min(qty, buffer.length - wPos);
        if (n > 0) byteBuffer.get(buffer, wPos, n);
        wPos += n;
        total -= n;
        while (total > 0) {
            buffers.add(buffer);
            if (buffers.size() > maxBlockCount) throw new IOException("buffer capacity exceeded (" + buffer.length + " * " + maxBlockCount + ")");
            buffer = new byte[buffer.length];
            n = Math.min(buffer.length, total);
            byteBuffer.get(buffer, 0, n);
            total -= n;
            wPos = n;
        }
        return qty;
    }
}
