package parser.pdf;

import parser.pdf.impl.PdfReaderImpl;
import parser.pdf.impl.PdfStream;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;

public class DocumentBuilder {
    public static Document parse(final File file) throws IOException, PdfException{
        final PdfReaderImpl pdfReader = new PdfReaderImpl();
        pdfReader.parse(file);
        return ((DefaultHandler) pdfReader.getContentHandler()).getDocument();
    }

    public static void main(String[] args) throws Exception {
        java.util.logging.Logger.getLogger("").setLevel(Level.FINE);
        for (Handler handler : java.util.logging.Logger.getLogger("").getHandlers()) {
            handler.setLevel(Level.FINE);
        }
//        final File file = new File("dip-91781-ko.pdf");
        final File file = new File("dip-75744-fr.pdf");
//        final File file = new File("dip-48982-it.pdf");
        final Document document = parse(file);
        System.out.printf("%s %s pages%n", document, document.getCatalog().getPages().getPageCount());
        for (final Font font: document.getFonts(0, 20)) System.out.printf("font: %s/%s%n", font.getSubtype(), font.getBaseFont());
        for (final Map.Entry<String, String> entry: document.getMetadata().entrySet()) System.out.printf("%s: %s%n", entry.getKey(), entry.getValue());
        final PdfStream stream = document.getCatalog().getPages().getPage(0).getContents().getst(0);
        System.out.printf("page 1 stream: %s%n", stream);
        stream.dump(System.out);
    }

}
