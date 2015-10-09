package parser.pdf.impl.codec;

import java.io.*;
import java.nio.charset.Charset;

public class Ascii85OutputStream extends OutputStream {
    private final OutputStream os;
    private int lineLength = -1;
    private final int[] buf = new int[5];
    private int val = 0;
    private int countIn = 0;
    private int countOut = 0;

    public Ascii85OutputStream(OutputStream os) {
        this.os = os;
    }

    public Ascii85OutputStream(OutputStream os, int lineLength) {
        this.os = os;
        this.lineLength = lineLength;
    }

    public void write(int b) throws IOException {
        convert(b, 0);
    }

    private void convert(int b, int countClose) throws IOException {
        if (countIn == 0){
            put(0x3C);
            put(0x7E);
        }
        val = (val * 0x100) + (b & 0xFF);
        countIn++;
        if ((countIn & 3) == 0){
            if (val == 0 && countClose == 0){
                put(0x7A);
            } else{
                for (int i = 0; i < 5; i++){
                    buf[4 - i] = val % 85;
                    val /= 85;
                }
                for (int i = 0; i < 5 - countClose; i++) put((0x21 + buf[i]) & 0xFF);
            }
        }
    }

    private void put(int b) throws IOException {
        os.write(b);
        countOut++;
        if (lineLength > 0 && countOut % lineLength == 0){
            os.write(0xD);
            os.write(0xA);
        }
    }

    public void close() throws IOException {
        int countClose = 0;
        while ((countIn & 3) != 0) convert(0, ++countClose);
        put(0x7E);
        put(0x3E);
        os.close();
    }

    public static void main(String[] args) throws IOException {
        final String quote = "Man is distinguished, not only by his reason, but by this singular passion from other animals, which is a lust of the mind, that by a perseverance of delight in the continued and indefatigable generation of knowledge, exceeds the short vehemence of any carnal pleasure.";
        final Charset charset = Charset.forName("US-ASCII");
        final InputStream is = new ByteArrayInputStream(quote.getBytes(charset));
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final OutputStream os = new BufferedOutputStream(new Ascii85OutputStream(baos, 75));
        int read;
        byte[] buffer = new byte[1024];
        while ((read = is.read(buffer)) != -1) os.write(buffer, 0, read);
        os.close();
        is.close();
        final String out = new String(baos.toByteArray(), charset);
        System.out.printf("%s%n", out);
    }
}
