package parser.pdf.impl.codec;

import java.io.IOException;
import java.io.InputStream;

public class PngFilterInputStream extends InputStream {
    private final InputStream in;
    private final int lineLength;
    private final byte[] tl;
    private int countIn = 0;
    private int countOut = 0;

    public PngFilterInputStream(InputStream in, int lineLength) {
        this.in = in;
        this.lineLength = lineLength;
        this.tl = new byte[2 * lineLength];
    }

    public int read() throws IOException {
        if (countOut % lineLength == 0){
            int alg = in.read();
            if (alg == -1)
                return -1;
            countIn++;
            System.arraycopy(tl, lineLength, tl, 0, lineLength);
            int count = 0;
            int read = 0;
            while (count < lineLength && read >= 0){
                read = in.read(tl, lineLength + count, lineLength - count);
                count += read;
            }
            if (count != lineLength) throw new IOException("scan line incomplete");
            countIn += count;
            switch(alg){
                case 0: // none
                    break;
                case 1: // sub
                    for (int i = 1; i < lineLength; i++) tl[lineLength + i] += tl[lineLength + i - 1];
                    break;
                case 2: // up
                    for (int i = 0; i < lineLength; i++) tl[lineLength + i] += tl[i];
                    break;
                case 3: // avg
                    for (int i = 0; i < lineLength; i++) tl[lineLength + i] += Math.floor((tl[i] + (i == 0 ? 0 : tl[lineLength + i - 1])) / 2);
                    break;
                case 4: // paeth
                    throw new IOException("unsupported predictor: paeth");
                default:
                    throw new IOException("unsupported predictor algorithm: " + alg);
            }
        }
        int off = countOut % lineLength;
        countOut++;
        return tl[lineLength + off] & 0xFF;
    }

    public void close() throws IOException {
        in.close();
    }
}
