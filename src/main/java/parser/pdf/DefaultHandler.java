package parser.pdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.pdf.impl.Context;
import parser.pdf.impl.PdfDictionary;
import parser.pdf.impl.XrefTable;

import java.io.File;
import java.util.Map;

public class DefaultHandler implements ContentHandler, ErrorHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultHandler.class);
    private Document document = new Document();

    public Document getDocument() throws PdfException {
        return document;
    }

    public void error(String message) throws PdfException {
        throw new PdfException(message);
    }
    
    public void startDocument(File file, String version) throws PdfException {
        document.put("Version", version.substring(1));
        document.setContext(new Context(file));
    }

    public void endDocument() throws PdfException {
        try{
            document.getContext().initializeCredentials(document);
        } catch(Exception x){
            throw new PdfException(x);
        }
    }

    public void crossReferenceTable(XrefTable xref) throws PdfException {
        document.getContext().addCrossReferenceTable(xref);
    }

    public void startTrailer(int position) throws PdfException {
    }

    public void endTrailer(PdfDictionary trailer) throws PdfException {
        for (Map.Entry<String, Object> entry: trailer.entrySet()) if (!document.containsKey(entry.getKey())) document.put(entry.getKey(), entry.getValue());
    }
}
