package parser.pdf;

import java.io.File;
import java.io.IOException;

public interface PdfReader {
    void setContentHandler(ContentHandler contentHandler);
    ContentHandler getContentHandler();
    void setErrorHandler(ErrorHandler errorHandler);
    ErrorHandler getErrorHandler();
    void parse(File file) throws IOException, PdfException;
}
