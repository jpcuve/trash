package parser.pdf.impl;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public class XrefSubsection implements XrefTable {
    private final Charset CHARSET_ASCII = Charset.forName("US-ASCII");
    private final RandomAccessFileInputStream rafis;
    private final int position;
    private final int from;
    private final int length;

    public XrefSubsection(final File file, int position, int from, int length) throws IOException {
        this.rafis = new RandomAccessFileInputStream(file, 0);
        this.position = position;
        this.from = from;
        this.length = length;
    }

    public boolean isReferencing(final int id){
        return id >= from && id < from + length;
    }

    public XrefEntry getEntry(int id) throws IOException {
        final int start = position + 20 * (id - from);
        rafis.adjust(start, 20);
        final byte[] ref = new byte[20];
        int read = rafis.read(ref);
        if (read != 20) throw new IOException("cannot read cross reference at position: " + start);
        try{
            long position = Long.parseLong(new String(ref, 0, 10, CHARSET_ASCII));
            int generation = Integer.parseInt(new String(ref, 11, 5 , CHARSET_ASCII));
            boolean inUse = ref[17] == 'n';
            return inUse ? new XrefInUse(position, generation) : new XrefFree(position, generation);
        }catch(NumberFormatException x){
            throw new IOException("cannot parse object position: " + new String(ref));
        }
    }

    public long getPosition(int id) throws IOException {
        final int start = position + 20 * (id - from);
        rafis.adjust(start, 20);
        final byte[] ref = new byte[20];
        int read = rafis.read(ref);
        if (read != 20) throw new IOException("cannot read cross reference at position: " + start);
        long position = -1;
        try{
            position = Long.parseLong(new String(ref, 0, 10, "US-ASCII"));
        }catch(NumberFormatException x){
            throw new IOException("cannot parse object position: " + new String(ref));
        }
        return position;
    }

    public void dump(Writer writer) throws IOException {
        final String nl = System.getProperty("line.separator");
        for (int i = 0; i < length; i++){
            int n = from + i;
            writer.write(Integer.toString(n));
            writer.write(": ");
            writer.write(getEntry(n).toString());
            writer.write(nl);
        }
    }

    public String toString() {
        return String.format("from:%s, length: %s", from, length);
    }
}
