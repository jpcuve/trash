package parser.pdf.impl.codec;

import java.io.*;

public class RC4OutputStream extends OutputStream {
    private final OutputStream os;
    private final byte[] s = new byte[256];
    int x = 0;
    int y = 0;

    public RC4OutputStream(final OutputStream os, final byte[] key) {
        this.os = os;
        for (int i = 0; i < s.length; i++) s[i] = (byte) i;
        int j = 0;
        for (int i = 0; i < s.length; i++) {
            j = (j + key[i % key.length] + s[i]) & 0xFF;
            swap(i, j);
        }
    }

    private void swap(int i, int j){
        byte b = s[i];
        s[i] = s[j];
        s[j] = b;
    }

    public void write(int b) throws IOException {
        x = (x + 1) & 0xFF;
        y = (y + s[x]) & 0xFF;
        swap(x, y);
        b = (b ^ s[(s[x] + s[y]) & 0xFF]) & 0xFF;
        os.write(b);
    }

    public static byte[] encode(final byte[] plainText, final byte[] key) throws IOException {
        final InputStream is = new BufferedInputStream(new ByteArrayInputStream(plainText));
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(plainText.length);
        final OutputStream os = new RC4OutputStream(baos, key);
        int read;
        byte[] buffer = new byte[1024];
        while ((read = is.read(buffer)) != -1) os.write(buffer, 0, read);
        os.close();
        is.close();
        return baos.toByteArray();
    }
}
