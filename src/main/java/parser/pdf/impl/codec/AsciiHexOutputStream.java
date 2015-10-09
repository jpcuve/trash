package parser.pdf.impl.codec;

import java.io.*;
import java.nio.charset.Charset;

public class AsciiHexOutputStream extends OutputStream {
    private static final String HEX = "0123456789abcdef";
    private final OutputStream os;
    private final boolean space;

    public AsciiHexOutputStream(OutputStream os) {
        this.os = os;
        this.space = false;
    }

    public AsciiHexOutputStream(OutputStream os, boolean space) {
        this.os = os;
        this.space = space;
    }

    public void write(int b) throws IOException {
        os.write(HEX.charAt((b & 0xF0) >> 4));
        os.write(HEX.charAt(b & 0xF));
        if (space) os.write(0x20);
    }

    public void close() throws IOException {
        os.close();
    }

    public static String encode(final byte[] data) throws IOException {
        final InputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final OutputStream os = new BufferedOutputStream(new AsciiHexOutputStream(baos));
        int read;
        byte[] buffer = new byte[1024];
        while ((read = is.read(buffer)) != -1) os.write(buffer, 0, read);
        os.close();
        is.close();
        return new String(baos.toByteArray(), Charset.forName("US-ASCII"));
    }

}
