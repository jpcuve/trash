package api.jaf;

/**
 * @author jpc
 */
public class BrolDataContentHandler extends GenericDataContentHandler {
    public BrolDataContentHandler() {
        super(Brol.OBJECT_DATA_FLAVOR, Brol.SERIALIZED_DATA_FLAVOR, Brol.TRANSFER_DATA_FLAVORS);
    }
}
