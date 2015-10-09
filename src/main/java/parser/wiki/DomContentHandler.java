package parser.wiki;

import org.w3c.dom.Document;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 30, 2009
 * Time: 12:58:35 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DomContentHandler extends ContentHandler {
    WikiConfiguration getConfiguration();
    Document getDocument();
}
