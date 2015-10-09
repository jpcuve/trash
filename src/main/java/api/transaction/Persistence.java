package api.transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jpc
 */
public class Persistence {
    private static Map<String, EntityManagerFactory> factories = new HashMap<String, EntityManagerFactory>();

    public static EntityManagerFactory createEntityManagerFactory(final String unitName){
        EntityManagerFactory entityManagerFactory = factories.get(unitName);
        if (entityManagerFactory == null){
            entityManagerFactory = new EntityManagerFactory();
            factories.put(unitName, entityManagerFactory);
        }
        return entityManagerFactory;
    }
}
