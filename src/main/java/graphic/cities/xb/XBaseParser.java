package graphic.cities.xb;

import graphic.cities.BinaryInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 3, 2010
 * Time: 1:42:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class XBaseParser {
    private static final Logger LOG = LoggerFactory.getLogger(XBaseParser.class);
    private final Charset CHARSET_ASCII = Charset.forName("US-ASCII");
    private final BinaryInputStream is;

    public XBaseParser(InputStream is) {
        this.is = new BinaryInputStream(is);
    }

    public void parse(final XBaseHandler handler) throws IOException {
        int version = is.read();
        int year = is.read();
        int month = is.read();
        int day = is.read();
        final Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1900 + year, month - 1, day);
        final Date updated = calendar.getTime();
        int recordCount = (int) is.readLong(4, true);
        int headerLength = (int) is.readLong(2, true);
        int recordLength = (int) is.readLong(2, true);
        is.skip(2);
        boolean incompleteTransaction = is.read() == 1;
        boolean encrypted = is.read() == 1;
        is.skip(12);
        int mdx = is.read();
        int language = is.read();
        is.skip(2);
        handler.initTable(version, updated, recordCount, headerLength, recordLength, incompleteTransaction, encrypted, mdx, language);
        int remaining = headerLength - 32;
        final List<byte[]> mask = new ArrayList<byte[]>();
        while (remaining > 1){
            final byte[] name = new byte[10];
            if (10 != is.read(name)) throw new IOException("cannot read field name");
            int l = 0;
            while (l < name.length && name[l] != 0) l++;
            final String fieldName = new String(name, 0, l, CHARSET_ASCII);
            is.skip(1);
            char type = (char) is.read();
            is.skip(4);
            int length = is.read();
            int decimalCount = is.read();
            mask.add(new byte[length]);
            is.skip(13);
            boolean index = is.read() == 1;
            handler.field(fieldName, type, length, decimalCount, index);
            remaining -= 32;
        }
        if (0xD != is.read()) throw new IOException("invalid end of header marker");
        remaining--;
        is.skip(remaining);
        handler.doneDefinition();
        // read records
        final String[] rec = new String[mask.size()];
        for (int i = 0; i < recordCount; i++){
            boolean valid = 0x20 == is.read();
            for (int j = 0; j < rec.length; j++){
                byte[] data = mask.get(j);
                is.read(data);
                rec[j] = new String(data, CHARSET_ASCII).trim();
            }
            handler.record(i, valid, rec);
        }
        if (0x1A != is.read()) throw new IOException("invalid end of file marker");
        handler.doneTable();
    }

    public static void main(String[] args) throws Exception {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("");
        logger.setLevel(Level.FINE);
        for (Handler handler: logger.getHandlers()){
            handler.setLevel(Level.FINE);
            handler.setFormatter(new Formatter(){
                @Override
                public String format(LogRecord logRecord) {
                    return String.format("%s%n", logRecord.getMessage());
                }
            });
        }
        final InputStream is = new FileInputStream("eur/bnd-political-boundary-a.dbf");
        final XBaseParser parser = new XBaseParser(is);
        parser.parse(new MySqlConversionHandler("bnd_political", new OutputStreamWriter(System.out)));
    }
}
