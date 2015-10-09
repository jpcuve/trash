package parser.wiki;

/**
 * @author jpc
 */
public interface ContentHandler {
    void startDocument() throws WikiException;
    void endDocument() throws WikiException;
    void startLine(int startingWhitespaceCount) throws WikiException;
    void endLine(String line) throws WikiException;
    void startTag(String localName) throws WikiException;
    void endTag(String localName) throws WikiException;
    void characters(char[] ch, int start, int length) throws WikiException;
}
