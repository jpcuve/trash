package parser.pdf.impl.codec;

import java.io.*;

public class RC4InputStream extends InputStream {
    private final InputStream in;
    private final byte[] s = new byte[256];
    int x = 0;
    int y = 0;

    public RC4InputStream(final InputStream in, final byte[] key) {
        this.in = in;
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

    public int read() throws IOException {
        int b = in.read();
        if (b != -1){
            x = (x + 1) & 0xFF;
            y = (y + s[x]) & 0xFF;
            swap(x, y);
            b = (b ^ s[(s[x] + s[y]) & 0xFF]) & 0xFF;
        }
        return b;
    }

    public static byte[] decode(final byte[] cypherText, final byte[] key) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(cypherText.length);
        final InputStream is = new BufferedInputStream(new RC4InputStream(new ByteArrayInputStream(cypherText), key));
        final OutputStream os = new BufferedOutputStream(baos);
        int read;
        byte[] buffer = new byte[1024];
        while ((read = is.read(buffer)) != -1) os.write(buffer, 0, read);
        os.close();
        is.close();
        return baos.toByteArray();
    }
}
