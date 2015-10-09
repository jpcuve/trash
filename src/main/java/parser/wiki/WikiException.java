package parser.wiki;

import org.xml.sax.SAXException;

/**
 * @author jpc
 */
public class WikiException extends SAXException {
    public WikiException() {
    }

    public WikiException(String message) {
        super(message);
    }

    public WikiException(Exception e) {
        super(e);
    }

    public WikiException(String message, Exception e) {
        super(message, e);
    }
}
