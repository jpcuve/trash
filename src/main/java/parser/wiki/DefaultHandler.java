package parser.wiki;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * @author jpc
 */
public class DefaultHandler implements ContentHandler, ErrorHandler {
    public void startDocument() throws WikiException {
        System.out.println("DefaultHandler.startDocument");
    }

    public void endDocument() throws WikiException {
        System.out.println("DefaultHandler.endDocument");
    }

    public void startLine(int startingWhitespaceCount) throws WikiException {
        System.out.println("DefaultHandler.startLine");
    }

    public void endLine(String line) throws WikiException {
        System.out.println("DefaultHandler.endLine: " + line);
    }

    public void startTag(String localName) throws WikiException {
        System.out.println("DefaultHandler.startTag " + ("\n".equals(localName) ? "<new line>" : localName));
    }

    public void endTag(String localName) throws WikiException {
        System.out.println("DefaultHandler.endTag " + ("\n".equals(localName) ? "<new line>" : localName));
    }

    public void characters(char[] ch, int start, int length) throws WikiException {
        System.out.println("DefaultHandler.characters: " + new String(ch, start, length));
    }

    public void warning(SAXParseException e) throws WikiException {
        System.out.println("DefaultHandler.warning");
    }

    public void error(SAXParseException e) throws WikiException {
        System.out.println("DefaultHandler.error");
    }

    public void fatalError(SAXParseException e) throws WikiException {
        System.out.println("DefaultHandler.fatalError");
    }
}
