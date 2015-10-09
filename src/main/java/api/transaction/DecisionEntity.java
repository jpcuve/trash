package api.transaction;

/**
 * @author jpc
 */
public class DecisionEntity implements Entity {
    private Object key;
    private String reference;

    public void setId(Object o) {
        this.key = o;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String toString() {
        return this.getClass().getName() + ":" + key;
    }
}
