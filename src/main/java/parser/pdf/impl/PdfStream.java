package parser.pdf.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.pdf.PdfException;
import parser.pdf.impl.codec.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.InflaterInputStream;

public class PdfStream extends PdfDictionary {
    private static final Logger LOG = LoggerFactory.getLogger(PdfStream.class);
    private long start;
    private int length;

    public void setStart(final long start){
        this.start = start;
    }

    private long getStart(){
        return start;
    }

    private int getLength() {
        if (length == 0) try{
            length = geti("Length");
        } catch(PdfException x){
            // ignore
        }
        return length;
    }

    public InputStream getRawInputStream() throws PdfException {
        try{
            return new RandomAccessFileInputStream(context.getFile(), getStart(), getLength());
        } catch(IOException x){
            throw new PdfException("cannot get positioned input stream", x);
        }
    }

    public InputStream getInputStream() throws PdfException {
        InputStream is = getRawInputStream();
        if (context.getKey() != null) is = new RC4InputStream(is, context.computeObjectKey(this));
        final Object o = get("Filter");
        if (o != null){
            final List<String> filters = new ArrayList<String>();
            if (o instanceof String) filters.add((String) o);
            if (o instanceof PdfArray) for (final Object e: ((PdfArray) o)) if (e instanceof String) filters.add((String) e);
            final List<PdfDictionary> params = new ArrayList<PdfDictionary>();
            final Object o2 = get("DecodeParms");
            if (o2 != null) {
                if (o2 instanceof PdfDictionary) params.add((PdfDictionary) o2);
                if (o2 instanceof PdfArray) for (Object e: ((PdfArray) o2)) params.add(e != null && e instanceof PdfDictionary ? (PdfDictionary) e : null);
            } else{
                for (final String filter: filters) params.add(null);
            }
            if (params.size() != filters.size()) throw new PdfException("filter/ decode parameters mismatch");
            int count = 0;
            for (final String filter: filters){
                if ("ASCIIHexDecode".equals(filter)){
                    is = new AsciiHexInputStream(is);
                } else if ("ASCII85Decode".equals(filter)){
                    is = new Ascii85InputStream(is);
                } else if ("FlateDecode".equals(filter)){
                    is = new InflaterInputStream(is);
                    if (params.get(count) != null){
                        int predictor = params.get(count).geti("Predictor");
                        int columns = params.get(count).geti("Columns");
                        switch(predictor){
                            case 0:
                            case 1:
                                is = new TiffFilterInputStream(is, columns, predictor);
                                break;
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                                is = new PngFilterInputStream(is, columns);
                                break;
                            default:
                                throw new PdfException("unrecognized predictor: " + predictor);
                        }

                    }
                } else throw new PdfException("unrecognized filter: " + filter);
                count++;
            }
        }
        return is;
    }

    public void dump(final OutputStream os) throws IOException, PdfException {
        final byte[] buf = new byte[64 * 1024];
        InputStream is = null;
        try{
            is = getInputStream();
            int total = 0;
            int read;
            while ((read = is.read(buf)) != -1) {
                total += read;
                os.write(buf, 0, read);
            }
            LOG.debug("bytes read: {}", total);
        } finally{
            if (is != null) try{
                is.close();
            } catch(IOException x){
                LOG.error("cannot close input stream", x);
            }
        }
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(String.format(" stream [start:0x%h, length:0x%h] endstream", getStart(), getLength()));
        return sb.toString();
    }
}
