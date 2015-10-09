package lang.dialect;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 5:26:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class DialectException extends Exception {
    public DialectException() {
    }

    public DialectException(String message) {
        super(message);
    }

    public DialectException(Throwable cause) {
        super(cause);
    }

    public DialectException(String message, Throwable cause) {
        super(message, cause);
    }
}
