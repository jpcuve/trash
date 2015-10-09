package api.jaf;

import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * @author jpc
 */
public class GenericDataContentHandler implements DataContentHandler {
    private final DataFlavor objectDataFlavor;
    private final DataFlavor serializedDataFlavor;
    private final DataFlavor[] dataFlavors;

    public GenericDataContentHandler(final DataFlavor objectDataFlavor, final DataFlavor serializedDataFlavor, final DataFlavor[] transferDataFlavors) {
        this.objectDataFlavor = objectDataFlavor;
        this.serializedDataFlavor = serializedDataFlavor;
        this.dataFlavors = transferDataFlavors;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return dataFlavors;
    }

    private Transferable read(final DataSource dataSource) throws IOException {
        try{
            final DataFlavor sourceDataFlavor = new DataFlavor(dataSource.getContentType());
            if (objectDataFlavor.equals(sourceDataFlavor)){
                return ((ObjectDataSource<Transferable>) dataSource).get();
            } else if (serializedDataFlavor.equals(sourceDataFlavor)){
                return (Transferable)((SerializedDataSource<Serializable>) dataSource).get();
            }
        } catch(ClassNotFoundException x){
            throw new IOException("class not available: " + dataSource.getContentType());
        }
        throw new IOException("cannot get object from data source:" + dataSource.getContentType());
    }

    public Object getTransferData(final DataFlavor dataFlavor, final DataSource dataSource) throws UnsupportedFlavorException, IOException {
        return read(dataSource).getTransferData(dataFlavor);
    }

    public Object getContent(final DataSource dataSource) throws IOException {
        try{
            return getTransferData(objectDataFlavor, dataSource);
        } catch(UnsupportedFlavorException x){
            // ignore
        }
        assert false: "flavor not supported???";
        return null;
    }

    public void writeTo(final Object o, final String mimeType, OutputStream outputStream) throws IOException {
        System.out.println("GenericDataContentHandler.writeTo");
    }
}
