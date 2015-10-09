package api.jaf;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author jpc
 */
public class ObjectDataSource<E> implements DataSource {
    private E e;
    private String mimeType;

    public ObjectDataSource(final E e, final String mimeType) {
        this.e = e;
        this.mimeType = mimeType;
    }

    public E get(){
        return e;
    }

    public String getMimeType() {
        return mimeType;
    }

    public InputStream getInputStream() throws IOException {
        throw new IOException("unsupported");
    }

    public OutputStream getOutputStream() throws IOException {
        throw new IOException("unsupported");
    }

    public String getContentType() {
        return mimeType;
    }

    public String getName() {
        return e.toString();
    }
}
