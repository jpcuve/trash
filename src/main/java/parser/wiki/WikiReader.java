package parser.wiki;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import java.io.IOException;

/**
 * @author jpc
 */
public interface WikiReader {
    void setContentHandler(ContentHandler contentHandler);
    ContentHandler getContentHandler();
    void setErrorHandler(ErrorHandler errorHandler);
    ErrorHandler getErrorHandler();
    void parse(InputSource inputSource) throws IOException, WikiException;
    void parse(String systemId) throws IOException, WikiException;
}
