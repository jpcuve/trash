package parser.pdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.pdf.impl.PdfReaderImpl;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;

public class ScanFolder {
    private static final Logger LOG = LoggerFactory.getLogger(ScanFolder.class);
    private static int countFiles = 0;
    private static int countErrors = 0;

    private static void recurse(final File folder){
        for (final File f: folder.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".pdf") || f.isDirectory();
            }
        })){
            if (f.isDirectory()) recurse(f);
            if (f.isFile() && f.length() > 0){
                countFiles++;
                final PdfReaderImpl pdfReader = new PdfReaderImpl();
                try{
                    pdfReader.parse(f);
                    final Document document = ((DefaultHandler) pdfReader.getContentHandler()).getDocument();
//                System.out.printf("%s (errors: %s): document: %s, %s%n", countFiles, countErrors, majorVersion.getAbsolutePath(), document);
                    int p = Math.min(3, document.getPageCount());
                    int fontCount = document.getFonts(0, p).size();
                    System.out.printf("%s: %s %s%n", f.getAbsolutePath(), document.getEncrypt() == null ? "not encrypted" : "encrypted", fontCount == 0 ? "not OCR !!!!!!!!!!!!!!!!!!!!!!!!!!!" : "ocr");
                    for (final Map.Entry<String, String> e: document.getMetadata().entrySet()){
                        System.out.printf(" %s: %s%n", e.getKey(), e.getValue());
                    }
/*
                    if (document.getCatalog().getMetadata() != null){
//                        document.getCatalog().getMetadata().dump(System.out);
//                        System.out.printf("%s%n", document.getCatalog().getMetadata().getPdfInfo());
                        for (final Map.Entry<String, String> entry: document.getCatalog().getMetadata().getPdfInfo().entrySet()){
                            System.out.printf(" %s: %s%n", entry.getKey(), entry.getValue());
                        }
                    }
*/
                } catch(Exception x){
                    countErrors++;
                    System.out.printf("%s (errors: %s): document: %s%n", countFiles, countErrors, f.getAbsolutePath());
                    LOG.error("exception scanning document: " + f.getAbsolutePath(), x);
                }





            }
        }

    }
    
    public static void main(String[] args) {
        final File f = new File(args.length > 0 ? args[0] : "c:/darts/decisions/02");
        if (f.isDirectory()) recurse(f);
    }
}
