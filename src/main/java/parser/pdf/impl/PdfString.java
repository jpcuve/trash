package parser.pdf.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.pdf.PdfException;
import parser.pdf.impl.codec.RC4InputStream;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PdfString implements Container {
    private static final Logger LOG = LoggerFactory.getLogger(PdfString.class);
    private static final Charset CHARSET_LATIN1 = Charset.forName("ISO-8859-1");
    private static final Charset CHARSET_UTF16 = Charset.forName("UTF-16");
    protected Context context;
    protected long position;
    private boolean hexa;
    private byte[] bytes = new byte[0];


    public PdfString(final byte[] data){
        if (data != null && data.length > 0){
            hexa = data[0] == '<';
            if (hexa){
                final List<Byte> list = new ArrayList<Byte>();
                boolean toggle = false;
                int acc = 0;
                for (int i = 1; i < data.length - 1; i++){
                    int off = "0123456789ABCDEFabcdef".indexOf(data[i]);
                    if (off != -1){
                        if (off > 15) off -= 6;
                        if (toggle){
                            acc = (acc << 4) + off;
                            list.add((byte) acc);
                            acc = 0;
                        } else{
                            acc = off;
                        }
                        toggle = !toggle;
                    }
                }
                if (toggle) list.add((byte) (acc << 4));
                this.bytes = new byte[list.size()];
                for (int i = 0; i < bytes.length; i++) bytes[i] = list.get(i);
            } else{
                this.bytes = new byte[data.length - 2];
                System.arraycopy(data, 1, bytes, 0, bytes.length);
            }
        }
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public Collection<Object> getChildren() {
        return Collections.emptyList();
    }

    public String stringValue() throws PdfException {
        try{
            final byte[] data = context.getKey() == null ? bytes : RC4InputStream.decode(bytes, context.computeObjectKey(this));
            boolean utf16 = data.length > 2 && data[0] == -2 && data[1] == -1;
            return utf16 ? new String(data, 2, data.length - 2, CHARSET_UTF16) : new String(data, 0, data.length, CHARSET_LATIN1);
        } catch(IOException x){
            throw new PdfException(x);
        }
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (hexa){
            sb.append('<');
            for (byte b: bytes) sb.append(String.format("%02x", b).toUpperCase());
            sb.append('>');
        } else{
            sb.append('(');
            sb.append(new String(bytes, CHARSET_LATIN1));
            sb.append(')');
        }
        return sb.toString();
    }
}
