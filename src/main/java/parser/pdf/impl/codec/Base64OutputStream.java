package parser.pdf.impl.codec;

import java.io.*;

public class Base64OutputStream extends OutputStream {
    private static final byte[] ALPHABET = { 0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x4A,0x4B,0x4C,0x4D,0x4E,0x4F,0x50,0x51,0x52,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5A,0x61,0x62,0x63,0x64,0x65,0x66,0x67,0x68,0x69,0x6A,0x6B,0x6C,0x6D,0x6E,0x6F,0x70,0x71,0x72,0x73,0x74,0x75,0x76,0x77,0x78,0x79,0x7A,0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,0x2B,0x2F };
    public static final byte PAD = 0x3D;

    private final OutputStream output;
    private int lineLength = 76;
    private int last = 0;
    private int countIn = 0;
    private int countOut = 0;

    public Base64OutputStream(final OutputStream output) {
        this.output = output;
    }

    public Base64OutputStream(final OutputStream output, final int lineLength) {
        this.output = output;
        if (lineLength > 76) throw new IllegalArgumentException("line length cannot exceed 76");
        this.lineLength = lineLength;
    }

    public void write(int b) throws IOException {
        int offset;
        switch(countIn){
            case 0:
                offset = (b & 0xFC) >> 2;
                put(ALPHABET[offset]);
                break;
            case 1:
                offset = ((last & 0x03) << 4) | ((b & 0xF0) >> 4);
                put(ALPHABET[offset]);
                break;
            case 2:
                offset = ((last & 0x0F) << 2) | ((b & 0xC0) >> 6);
                put(ALPHABET[offset]);
                offset = (b & 0x3F);
                put(ALPHABET[offset]);
                break;
        }
        countIn = (countIn + 1) % 3;
        last = b;
    }

    private void put(int b) throws IOException {
        output.write(b);
        if (lineLength > 0){
            countOut = (countOut + 1) % lineLength;
            if (countOut == 0){
                output.write(0x0D);
                output.write(0x0A);
            }
        }
    }

    public void close() throws IOException {
        int offset;
        switch(countIn){
            case 1:
                offset = (last & 0x03) << 4;
                put(ALPHABET[offset]);
                put(PAD);
                output.write(PAD);
                break;
            case 2:
                offset = (last & 0x0F) << 2;
                put(ALPHABET[offset]);
                output.write(PAD);
                break;
        }
        output.close();
    }


    public static String encode(final byte[] data) throws IOException {
        class AsciiStringOutputStream extends OutputStream{
            private StringBuilder sb = null;

            public AsciiStringOutputStream(int size) {
                this.sb = new StringBuilder(size);
            }

            public void write(int b) throws IOException {
                sb.append((char)b);
            }

            public String toString() {
                return sb.toString();
            }
        }
        final AsciiStringOutputStream asos = new AsciiStringOutputStream(((data.length + 1) * 4) / 3);
        final InputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
        final OutputStream os = new BufferedOutputStream(new Base64OutputStream(asos));
        int read;
        byte[] buffer = new byte[1024];
        while ((read = is.read(buffer)) != -1) os.write(buffer, 0, read);
        os.close();
        is.close();
        return asos.toString();
    }
}

