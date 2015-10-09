package parser.pdf.impl;

import parser.pdf.PdfException;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class XrefStreamSection implements XrefTable {
    private final byte[] data;
    private final List<Integer> widths;
    private int position;
    private int from;
    private int length;
    private int entryLength;

    public XrefStreamSection(byte[] data, int position, int from, int length, List<Integer> widths) throws PdfException {
        this.data = data;
        this.position = position;
        this.from = from;
        this.length = length;
        this.widths = widths;
        entryLength = 0;
        for (int width: widths) entryLength += width;
        if (position + length * entryLength > data.length) throw new PdfException("cross reference stream subsection size mismatch");
    }

    public static long extractLong(final byte[] b, final int start, final int length){
        long val = 0;
        for (int i = 0; i < length; i++) if (start + i < b.length) {
            val <<= 8;
            byte v = b[start + i];
            val += v < 0 ? v + 256 : v;
        }
        return val;
    }

    public boolean isReferencing(final int id){
        return id >= from && id < from + length;
    }

    public XrefEntry getEntry(int id) throws IOException {
        int offset = (position + id - from) * entryLength;
        int type = (int) extractLong(data, offset, widths.get(0));
        long a = extractLong(data, offset + widths.get(0), widths.get(1));
        int b = (int) extractLong(data, offset + widths.get(0) + widths.get(1), widths.get(2));
        switch(type){
            case 0:
                return new XrefFree(a, b);
            case 1:
                return new XrefInUse(a, b);
            case 2:
                return new XrefCompressedObject(a, b);
        }
        throw new IOException("unrecognized entry type: " + type);
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
