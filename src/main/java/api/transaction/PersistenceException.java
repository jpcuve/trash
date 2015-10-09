package api.transaction;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 27, 2008
 * Time: 5:26:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersistenceException extends Exception {
    public PersistenceException() {
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
