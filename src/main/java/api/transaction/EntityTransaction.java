package api.transaction;

/**
 * @author jpc
 */
public class EntityTransaction {
    private final int count;
    private boolean active;

    public EntityTransaction(final int count) {
        this.count = count;
    }

    public void begin(){
        System.out.printf("beginning: %s%n", this);
        active = true;
    }

    public void commit() throws PersistenceException {
        System.out.printf("committing: %s%n", this);
        active = false;
    }

    public void rollback(){
        System.out.printf("rolling back: %s%n", this);
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public String toString() {
        return "entity transaction " + count;
    }
}
