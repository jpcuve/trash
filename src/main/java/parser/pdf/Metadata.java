package parser.pdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import parser.pdf.impl.PdfStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Metadata extends PdfStream {
    private static final Logger LOG = LoggerFactory.getLogger(Metadata.class);
    private Map<String, String> pdfInfo = null;

    public Map<String, String> getPdfInfo() {
        if (pdfInfo == null && "XML".equals(get("Subtype"))) try {
            pdfInfo = new HashMap<String, String>();
            final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            final SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(getInputStream(), new org.xml.sax.helpers.DefaultHandler(){
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if ("rdf:Description".equals(qName)) for (int i = 0; i < attributes.getLength(); i++){
                        final String attributeQName = attributes.getQName(i);
                        if (attributeQName.startsWith("pdf:")){
                            final String key = attributeQName.substring(4);
                            final String val = attributes.getValue(i);
                            pdfInfo.put(key, val);
                        }
                    }
                }
            });
        } catch (ParserConfigurationException e) {
            LOG.error("parser configuration error", e);
        } catch (SAXException e) {
            LOG.error("SAX error", e);
        } catch (IOException e) {
            LOG.error("cannot get input stream", e);
        } catch (PdfException e) {
            LOG.error("cannot get input stream from pdf stream", e);
        }
        return pdfInfo;
    }
}
