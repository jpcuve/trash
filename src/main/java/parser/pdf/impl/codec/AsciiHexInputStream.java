package parser.pdf.impl.codec;

import java.io.*;

public class AsciiHexInputStream extends InputStream {
    private final String HEX = "0123456789ABCDEFabcdef";
    private final InputStream in;

    public AsciiHexInputStream(InputStream in) {
        this.in = in;
    }

    public int read() throws IOException {
        int b;
        int acc = 0;
        for (int i = 0; i < 2; i++){
            do b = in.read(); while (b != -1 && Character.isWhitespace(b));
            if (b == -1) return i == 0 ? -1 : acc;
            int v = HEX.indexOf(b);
            if (v == -1) throw new IOException("invalid character is hex stream: " + b);
            if (v > 15) v -= 6;
            acc += i == 0 ? v << 4 : v;
        }
        return acc;
    }

    public void close() throws IOException {
        in.close();
    }

    public static byte[] decode(final String text) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(text.length());
        final InputStream is = new AsciiHexInputStream(new ByteArrayInputStream(text.getBytes("US-ASCII")));
        final OutputStream os = new BufferedOutputStream(baos);
        int read;
        byte[] buffer = new byte[1024];
        while ((read = is.read(buffer)) != -1) os.write(buffer, 0, read);
        os.close();
        is.close();
        return baos.toByteArray();
    }
}
