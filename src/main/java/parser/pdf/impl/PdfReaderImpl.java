package parser.pdf.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfReaderImpl implements PdfReader {
    private static final Logger LOG = LoggerFactory.getLogger(PdfReaderImpl.class);
    private static final String CHARSET = "ISO-8859-1";
    private final byte[] buffer = new byte[65536];
    private ContentHandler contentHandler;
    private ErrorHandler errorHandler;

    public PdfReaderImpl() {
        final DefaultHandler defaultHandler = new DefaultHandler();
        this.contentHandler = defaultHandler;
        this.errorHandler = defaultHandler;
    }

    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void parse(File file) throws IOException, PdfException {
        final LineReader reader = new LineReader(file, CHARSET, 4096);
        String line;
        line = reader.readLine();
        if (line == null) errorHandler.error("cannot read header");
        if (line != null && !line.startsWith("%PDF-")) errorHandler.error("invalid header (does not start with '%PDF-')");
        contentHandler.startDocument(file, line);
        final List<String> lastLines = new ArrayList<String>();
        int xrefPosition = -1;
        for (int i = 0; i < 16 && xrefPosition <= 0; i++){
            reader.reposition(file.length() - (i * 128));
            while ((line = reader.readLine()) != null) if (line.length() > 0) lastLines.add(line.trim());
            final int size = lastLines.size();
            for (int j = size - 1; j >= 0 && xrefPosition < 0; j--){
                if ("startxref".equals(lastLines.get(j))) try{
                    xrefPosition = Integer.parseInt(lastLines.get(j + 1));
                } catch(NumberFormatException x){
                    // ignore
                }
            }
        }
        if (xrefPosition == -1){
            // linearized case
            reader.reposition(0);
            for (int i = 0; i < 512; i++){
                xrefPosition = reader.getTotalByteCount();
                line = reader.readLine();
                if ("xref".equals(line)) break;
                xrefPosition = -1;
            }
        }
        if (xrefPosition < 0) errorHandler.error("cannot find cross reference table position in file");
        // read cross reference table
        recurseCrossReferenceTables(file, xrefPosition);
        contentHandler.endDocument();
    }

    private void recurseCrossReferenceTables(final File file, final int xrefPosition) throws IOException, PdfException {
        LOG.debug("xref table position: {}", String.format("0x%h", xrefPosition));
        final LineReader reader = new LineReader(file, CHARSET, 4096);
        reader.reposition(xrefPosition);
        String line = reader.readLine();
        if ("xref".equals(line)){
            String subsection;
            int position = xrefPosition + reader.getLastLineByteCount();
            do {
                reader.reposition(position);
                subsection = reader.readLine();
                if (subsection != null){
                    int space = subsection.indexOf(' ');
                    if (space != -1) try{
                        int from = Integer.parseInt(subsection.substring(0, space));
                        int length = Integer.parseInt(subsection.substring(space + 1).trim());
                        LOG.debug("xref entries, from: {} length: {}", from, length);
                        position += reader.getTotalByteCount();
                        final XrefSubsection xrefTable = new XrefSubsection(file, position, from, length);
                        contentHandler.crossReferenceTable(xrefTable);
                        position += length * 20;
                    } catch(NumberFormatException x){
                        errorHandler.error(x.getMessage());
                    }
                }
            } while (!"trailer".equals(subsection) && subsection != null);
            if (subsection != null){
                final int trailerPosition = position + reader.getTotalByteCount();
                LOG.debug("trailer position: {}", trailerPosition);
                contentHandler.startTrailer(trailerPosition);
                final PdfDictionary trailer = new Parser(file, trailerPosition).parseDictionary();
                if (trailer == null) errorHandler.error("cannot parse trailer dictionnary");
                LOG.debug("trailer: {}", trailer);
                contentHandler.endTrailer(trailer);
                if (trailer.containsKey("XRefStm")) recurseCrossReferenceTables(file, trailer.geti("XRefStm"));
                if (trailer.containsKey("Prev")) recurseCrossReferenceTables(file, trailer.geti("Prev"));
            }
        } else{
            final PdfObject object = new Parser(file, xrefPosition).parseObject();
            if (object == null){
                LOG.error("invalid cross reference stream at position: {}", xrefPosition);
                return;
            }
            if (!(object.getContent() instanceof XRef)){
                LOG.error("object referenced by XRefStm is not an XRef at position: {}", xrefPosition);
                return;
            }
            final XRef xref = (XRef) object.getContent();
            xref.setContext(new Context(file)); // context is not built yet
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            int read;
            try{
                is = xref.getInputStream();
                baos = new ByteArrayOutputStream();
                while ((read = is.read(buffer)) != -1)
                    baos.write(buffer, 0, read);
            } finally{
                if (baos != null) baos.close();
                if (is != null) is.close();
            }
            final byte[] data = baos.toByteArray();
            final PdfArray w = xref.geta("W");
            final List<Integer> widths = w.extractIntegers();
            int entryLength = 0;
            for (final int width: widths) entryLength += width;
            final PdfArray index = xref.geta("Index");
            if (index == null){
                contentHandler.crossReferenceTable(new XrefStreamSection(data, 0, 0, data.length / entryLength , widths));
            } else{
                final List<Integer> indexes = index.extractIntegers();
                int position = 0;
                for (int i = 0; i < indexes.size(); i += 2){
                    final Integer from = indexes.get(i);
                    final Integer length = indexes.get(i + 1);
                    contentHandler.crossReferenceTable(new XrefStreamSection(data, position, from, length, widths));
                    position += length;
                }
            }
            if (xref.containsKey("Prev")) recurseCrossReferenceTables(file, xref.geti("Prev"));
        }
    }

}
