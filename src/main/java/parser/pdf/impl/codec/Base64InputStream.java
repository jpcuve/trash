package parser.pdf.impl.codec;

import java.io.*;

public class Base64InputStream extends InputStream {
    public static final byte PAD = 0x3D;
    private final InputStream input;
    private int last = 0;
    private int countOut = 0;

    public Base64InputStream(final InputStream input) {
        this.input = input;
    }

    public int read() throws IOException {
        int read = -1;
        int b = offset();
        if (b == -1) return -1;
        switch (countOut){
            case 0:
                last = b;
                b = offset();
                if (b == -1) return -1;
                read = (last << 2) | b >> 4;
                break;
            case 1:
                read = ((last & 0x0F) << 4) | ((b & 0x3C) >> 2);
                break;
            case 2:
                read = ((last & 0x03) << 6) | b;
                break;
        }
        countOut = (countOut + 1) % 3;
        last = b;
        return read;
    }

    private int offset() throws IOException {
        int b = input.read();
        while (b == 0x0D || b == 0x0A) b = input.read();
        switch(b){
            case -1:
                return -1;
            case PAD:
                return -1;
            case 0x2B:
                return 0x3E;
            case 0x2F:
                return 0x3F;
            default:
                if (b > 0x40 && b < 0x5B) return b - 0x41;
                else if (b > 0x60 && b < 0x7B) return b - 0x47;
                else if (b > 0x2F && b < 0x3A) return b + 0x04;
        }
        throw new IOException("badly encoded: " + b);
    }

    public void close() throws IOException {
        input.close();
    }

    public static byte[] decode(final String text) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(text.length());
        final InputStream is = new BufferedInputStream(new Base64InputStream(new ByteArrayInputStream(text.getBytes("US-ASCII"))));
        final OutputStream os = new BufferedOutputStream(baos);
        int read;
        byte[] buffer = new byte[1024];
        while ((read = is.read(buffer)) != -1) os.write(buffer, 0, read);
        os.close();
        is.close();
        return baos.toByteArray();
    }
}
