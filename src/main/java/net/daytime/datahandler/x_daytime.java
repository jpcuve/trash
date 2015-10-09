package net.daytime.datahandler;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import java.awt.datatransfer.DataFlavor;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 6, 2006
 * Time: 9:16:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class x_daytime implements DataContentHandler {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static ActivationDataFlavor ADF1, ADF2, ADF3, ADF4;
    private static DataFlavor[] ADFs;

    static {
        ADF1 = new ActivationDataFlavor(InputStream.class, "application/x-daytime; class=java.io.InputStream", "daytime object as raw InputStream");
        ADF2 = new ActivationDataFlavor(Date.class, "application/x-daytime; class=java.util.Date", "daytime object as Date");
        ADF3 = new ActivationDataFlavor(String.class, "text/plain; class=java.lang.String; charset=us-ascii", "daytime object as String");
        ADFs = new DataFlavor[] { ADF1, ADF2, ADF3, DataFlavor.getTextPlainUnicodeFlavor() };
    }

    public Object getContent(DataSource dataSource) throws IOException {
        int read;
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = new BufferedInputStream(dataSource.getInputStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream  bos  = new BufferedOutputStream(baos);
        while ((read = bis.read(buffer)) != -1) bos.write(buffer, 0, read);
        bos.close();
        bis.close();
        try{
            return DATE_FORMAT.parse(new String(baos.toByteArray(), "US-ASCII"));
        } catch(ParseException x){
            IOException iox = new IOException("unable to parse date");
            iox.initCause(x);
            throw iox;
        }
    }

    public Object getTransferData(DataFlavor dataFlavor, DataSource dataSource) throws IOException {
        if (ADF1.equals(dataFlavor)) {
            return dataSource.getInputStream();
        } else if (ADF2.equals(dataFlavor)) {
            return getContent(dataSource);
        } else if (ADF3.equals(dataFlavor)){
            return getContent(dataSource).toString();
        } else if (DataFlavor.getTextPlainUnicodeFlavor().equals(dataFlavor)) {
            return new ByteArrayInputStream(getContent(dataSource).toString().getBytes("utf-16le"));
        } throw new IOException("unsupported data flavor: " + dataFlavor);
    }

    public DataFlavor[] getTransferDataFlavors() {
        return ADFs;
    }

    public void writeTo(Object obj, String mimeType, OutputStream os) throws IOException {
        if(obj instanceof Date) {
            os.write(DATE_FORMAT.format((Date)obj).getBytes("US-ASCII"));
        } else if (obj instanceof InputStream) {
            InputStream in = (InputStream)obj;
            int b;
            while ((b = in.read()) >= 0) os.write(b);
        } else {
            throw new IOException("unsupported class: " + obj.getClass().getName());
        }
    }
}
