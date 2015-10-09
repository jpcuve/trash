package api.jaf;

import javax.activation.CommandObject;
import javax.activation.DataHandler;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * @author jpc
 */
public class TextViewer implements CommandObject {
    private String verb;
    private DataHandler dataHandler;

    public void setCommandContext(final String verb, final DataHandler dataHandler) throws IOException {
        this.verb = verb;
        this.dataHandler = dataHandler;
        // execute; this is getLicenseTypeString bit more complex normally

        try{
            final ObjectInputStream is = new ObjectInputStream((InputStream) dataHandler.getTransferData(DataFlavor.stringFlavor));
            final String s = (String) is.readObject();
            System.out.printf("%s%n", s);
            is.close();
        } catch(Exception x){
            x.printStackTrace();
        }
    }
}
