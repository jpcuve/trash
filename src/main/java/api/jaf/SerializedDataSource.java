package api.jaf;

import javax.activation.DataSource;
import java.io.*;

/**
 * @author jpc
 */
public class SerializedDataSource<E extends Serializable> implements DataSource {
    private byte[] data;
    private String name;
    private String mimeType;

    public SerializedDataSource(final E e){
        set(e);
    }

    public E get(){
        ObjectInputStream is = null;
        try{
            is = new ObjectInputStream(getInputStream());
            return (E) is.readObject();
        } catch(Exception x){
            x.printStackTrace();
        } finally{
            if (is != null) try{
                is.close();
            } catch(IOException x){
                x.printStackTrace();
            }
        }
        return null;
    }

    public void set(E e){
        this.name = e.toString();
        this.mimeType = "application/x-java-serialized-object; class=" + e.getClass().getName();
        ObjectOutputStream os = null;
        try{
            os = new ObjectOutputStream(getOutputStream());
            os.writeObject(e);
        } catch(IOException x){
            x.printStackTrace();
        } finally{
            if (os != null) try{
                os.close();
            } catch(IOException x){
                x.printStackTrace();
            }
        }
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(data);
    }

    public OutputStream getOutputStream() throws IOException {
        return new ByteArrayOutputStream(){
            public void close() throws IOException {
                super.close();
                data = toByteArray();
            }
        };
    }

    public String getContentType() {
        return mimeType;
    }

    public String getName() {
        return name;
    }
}
