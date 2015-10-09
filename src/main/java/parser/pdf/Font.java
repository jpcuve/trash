package parser.pdf;

import parser.pdf.impl.PdfDictionary;

public class Font extends PdfDictionary {

    public String getSubtype() throws PdfException{
        return getn("Subtype");
    }

    public String getBaseFont() throws PdfException{
        return getn("BaseFont");
    }

}
