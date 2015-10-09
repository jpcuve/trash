package net.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Sep 28, 2005
 * Time: 9:26:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class NioUtility {
    public static int bufferSize = 32768;
    public static final int DEFAULT_TIMEOUT = 2000;

    public static int pump(ReadableByteChannel rbc, WritableByteChannel wbc) throws IOException {
        return pump(rbc, wbc, DEFAULT_TIMEOUT);
    }

    public static int pump(ReadableByteChannel rbc, WritableByteChannel wbc, long readTimeout) throws IOException {
        int written = 0;
        long beg = System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        while(true){
            int read = rbc.read(buffer);
            if (read == -1 && buffer.position() == 0) break;
            long now = System.currentTimeMillis();
            if (read == 0 && now - beg > readTimeout) throw new IOException("read timeout on readable byte channel: " + rbc);
            if (read > 0) beg = now;
            buffer.flip();
            written += wbc.write(buffer);
            buffer.compact();
        }
        return written;
    }

    public static int pump(InputStream is, WritableByteChannel wbc) throws IOException {
        int written = 0;
        byte[] bytes = new byte[bufferSize];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int read;
        while ((read = is.read(bytes, buffer.position(), buffer.remaining())) != -1 || buffer.position() > 0){
            if (read != -1) buffer.position(buffer.position() + read);
            buffer.flip();
            written += wbc.write(buffer);
            buffer.compact();
        }
        return written;
    }

    public static int pump(ReadableByteChannel rbc, OutputStream os) throws IOException {
        return pump(rbc, os, DEFAULT_TIMEOUT);
    }

    public static int pump(ReadableByteChannel rbc, OutputStream os, long readTimeout) throws IOException {
        int written = 0;
        long beg = System.currentTimeMillis();
        byte[] bytes = new byte[bufferSize];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        while(true){
            int read = rbc.read(buffer);
            if (read == -1) break;
            long now = System.currentTimeMillis();
            if (read == 0 && now - beg > readTimeout) throw new IOException("read timeout on readable byte channel: " + rbc);
            if (read > 0) beg = now;
            buffer.flip();
            os.write(bytes, 0, buffer.limit());
            written += buffer.limit();
            buffer.clear();
        }
        return written;
    }

    public static void main(String[] args) throws IOException {
        byte[] bytes = new byte[100];
        for (int i = 0; i < bytes.length; i++) bytes[i] = (byte)i;
        MemoryBuffer buffer = new MemoryBuffer(5, 1);
        NioUtility.pump(new ByteArrayInputStream(bytes), buffer);
        buffer.close();
        System.out.printf("memory channel size: %s\n", buffer.getBuffer().length);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        NioUtility.pump(buffer, baos);
        baos.close();
        System.out.printf("byte array output stream size: %s\n", baos.toByteArray().length);
    }



}
