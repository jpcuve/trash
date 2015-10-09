package parser.pdf;

import parser.pdf.impl.PdfDictionary;
import parser.pdf.impl.XrefTable;

import java.io.File;

public interface ContentHandler {
    public void startDocument(File file, String version) throws PdfException;
    public void endDocument() throws PdfException;
    public void crossReferenceTable(XrefTable xrefTable) throws PdfException;
    public void startTrailer(int position) throws PdfException;
    public void endTrailer(PdfDictionary trailer) throws PdfException;
}
