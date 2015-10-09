package parser.wiki;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import parser.wiki.impl.WikiReaderImpl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jpc
 */
public class WikiDocumentBuilderFactory extends DocumentBuilderFactory {
    private final Map<String, Object> attributes = new HashMap<String, Object>();
    private final Map<String, Boolean> features = new HashMap<String, Boolean>();
    private final DocumentBuilder documentBuilder;

    private class WikiDocumentBuilder extends DocumentBuilder{
        private final DomContentHandler domContentHandler;
        private final WikiReaderImpl wikiReader;

        private WikiDocumentBuilder(final DomContentHandler domContentHandler) {
            this.domContentHandler = domContentHandler;
            this.wikiReader = new WikiReaderImpl(domContentHandler.getConfiguration());
            wikiReader.setContentHandler(domContentHandler);
        }

        public Document parse(InputSource is) throws SAXException, IOException {
            wikiReader.parse(is);
            return domContentHandler.getDocument();
        }

        public boolean isNamespaceAware() {
            return false;
        }

        public boolean isValidating() {
            return false;
        }

        public void setEntityResolver(EntityResolver er) {
        }

        public void setErrorHandler(ErrorHandler eh) {
            wikiReader.setErrorHandler(eh);
        }

        public Document newDocument() {
            return documentBuilder.newDocument();
        }

        public DOMImplementation getDOMImplementation() {
            return null;
        }
    }

    public WikiDocumentBuilderFactory() throws ParserConfigurationException {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
        attributes.put("domContentHandlerClass", XHtmlContentHandler.class);
    }

    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        final Class domContentHandlerClass = (Class) attributes.get("domContentHandlerClass");
        return new WikiDocumentBuilder(new XHtmlContentHandler(documentBuilder, documentBuilder.newDocument()));
    }

    public void setAttribute(String name, Object value) throws IllegalArgumentException {
        attributes.put(name, value);
    }

    public Object getAttribute(String name) throws IllegalArgumentException {
        return attributes.get(name);
    }

    public void setFeature(String name, boolean value) throws ParserConfigurationException {
        features.put(name, value);
    }

    public boolean getFeature(String name) throws ParserConfigurationException {
        final Boolean value = features.get(name);
        return value == null ? false : value;
    }
}
