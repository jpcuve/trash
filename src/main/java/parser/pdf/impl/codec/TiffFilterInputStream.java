package parser.pdf.impl.codec;

import java.io.IOException;
import java.io.InputStream;

public class TiffFilterInputStream extends InputStream {
    private final InputStream in;
    private final int lineLength;
    private final int algorithm;

    public TiffFilterInputStream(InputStream in, int lineLength, int algorithm) {
        this.in = in;
        this.lineLength = lineLength;
        this.algorithm = algorithm;
    }

    public int read() throws IOException {
        return in.read();
    }

    public void close() throws IOException {
        in.close();
    }
}
