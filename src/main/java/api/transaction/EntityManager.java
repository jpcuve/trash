package api.transaction;

/**
 * @author jpc
 */
public class EntityManager {
    private Log log = new Log();
    private final int count;
    private final EntityTransaction transaction;


    public EntityManager(final int count) {
        System.out.printf("instantiating: %s%n", this);
        this.count = count;
        this.transaction = new EntityTransaction(count);
    }

    public Query createNamedQuery(final String s){
        return new Query();
    }

    public <E extends Entity> E find(final Class<E> clazz, Object key){
        try{
            final E entity = clazz.newInstance();
            entity.setId(key);
            return entity;
        } catch(Exception x){
            // ignore
        }
        return null;
    }

    public void persist(final Object o){
        log.debug(String.format("persisting: %s", o));
    }

    public void merge(final Object o){
        log.debug(String.format("merging: %s", o));

    }

    public void remove(final Object o){
        log.debug(String.format("removing: %s", o));
    }

    public EntityTransaction getTransaction() {
        return transaction;
    }

    public void close(){
        System.out.printf("closing: %s%n", this);
    }

    public String toString() {
        return "entity manager " + count;
    }
}
