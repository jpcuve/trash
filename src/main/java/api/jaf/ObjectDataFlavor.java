package api.jaf;

import java.awt.datatransfer.DataFlavor;

/**
 * @author jpc
 */
public class ObjectDataFlavor extends DataFlavor {
    public ObjectDataFlavor(final Class clazz, final String name) {
        super("application/x-" + name + ";class=" + clazz.getName(), name);
    }
}
