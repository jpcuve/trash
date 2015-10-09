package parser.pdf;

import parser.pdf.impl.PdfDictionary;

public class Catalog extends PdfDictionary {
    private Metadata metadata;
    private Pages pages;

    public Metadata getMetadata() throws PdfException {
        if (metadata == null && containsKey("Metadata")) metadata = (Metadata) getd("Metadata");
        return metadata;
    }

    public Pages getPages() throws PdfException {
        if (pages == null) pages = (Pages) getd("Pages");
        return pages;
    }

}
