package parser.pdf;

public interface ErrorHandler {
    void error(final String message) throws PdfException;
}
