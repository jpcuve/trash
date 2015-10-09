package parser.pdf.impl.codec;

import java.io.IOException;
import java.io.InputStream;

public class Ascii85InputStream extends InputStream {
    private final InputStream in;
    private int val = 0;
    private int countIn = 0;
    private int countOut = 0;

    public Ascii85InputStream(InputStream in) {
        this.in = in;
    }

    public int read() throws IOException {
        int b = in.read();
        if (countIn == 0){
            if (b != 0x3C) throw new IOException("invalid prolog");
            b = in.read();
            if (b != 0x7E) throw new IOException("invalid prolog");
            b = in.read();
        }
        val = val * 85 + ((b - 0x21) & 0xFF);
        return b;
    }

    public void close() throws IOException {
        in.close();
    }
}
