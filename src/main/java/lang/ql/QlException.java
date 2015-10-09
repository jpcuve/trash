package lang.ql;

/**
 * @author jpc
 */
public class QlException extends Exception {
    public QlException() {
    }

    public QlException(String message) {
        super(message);
    }

    public QlException(String message, Throwable cause) {
        super(message, cause);
    }

    public QlException(Throwable cause) {
        super(cause);
    }
}
