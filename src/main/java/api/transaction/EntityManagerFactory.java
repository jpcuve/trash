package api.transaction;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 27, 2008
 * Time: 5:13:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class EntityManagerFactory {
    public static int count = 0;
    public EntityManager createEntityManager() {
        return new EntityManager(count++);
    }
}
