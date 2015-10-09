package api.jaf;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author jpc
 */
public class Brol implements Transferable, Serializable {
    public static final DataFlavor OBJECT_DATA_FLAVOR = new ObjectDataFlavor(Brol.class, Brol.class.getSimpleName());
    public static final DataFlavor SERIALIZED_DATA_FLAVOR = new DataFlavor(Brol.class, OBJECT_DATA_FLAVOR.getHumanPresentableName());
    public static final DataFlavor[] TRANSFER_DATA_FLAVORS = new DataFlavor[]{ OBJECT_DATA_FLAVOR, SERIALIZED_DATA_FLAVOR, DataFlavor.stringFlavor };

    public DataFlavor[] getTransferDataFlavors() {
        return TRANSFER_DATA_FLAVORS;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (final DataFlavor dataFlavor: TRANSFER_DATA_FLAVORS) if (dataFlavor.equals(flavor)) return true;
        return false;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
        int match = -1;
        for (int i = 0; i < TRANSFER_DATA_FLAVORS.length && match < 0; i++) if (TRANSFER_DATA_FLAVORS[i].equals(flavor)) match = i;
        switch(match){
            case 0:
                return this;
            case 1:
                return new SerializedDataSource<Brol>(this).getInputStream();
            case 2:
                return new SerializedDataSource<String>(this.toString()).getInputStream();
        }
        return null;
    }
}
