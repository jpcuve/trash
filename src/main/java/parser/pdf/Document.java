package parser.pdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.pdf.impl.PdfDictionary;
import parser.pdf.impl.PdfString;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Document extends PdfDictionary {
    private static final Logger LOG = LoggerFactory.getLogger(Document.class);
    private Catalog catalog;
    private PdfDictionary info;
    private PdfDictionary encrypt;
    private Set<Font> fonts;

    public int getSize() throws PdfException{
        return geti("Size");
    }

    public Catalog getCatalog() throws PdfException {
        if (catalog == null) catalog = (Catalog) getd("Root");
        return catalog;
    }

    public PdfDictionary getInfo() throws PdfException {
        if (info == null && containsKey("Info")) info = getd("Info");
        return info;
    }

    public PdfDictionary getEncrypt() throws PdfException {
        if (encrypt == null && containsKey("Encrypt")) encrypt = getd("Encrypt");
        return encrypt;
    }

    public int getPageCount() throws PdfException {
        return getCatalog() == null || getCatalog().getPages() == null ? 0 : getCatalog().getPages().getPageCount();
    }

    public Map<String, String> getMetadata() {
        final Map<String, String> map = new HashMap<String, String>();
        try{
            if (getInfo() != null) for (final String key: info.keySet()){
                final Object val = context.resolve(info.get(key));
                if (val instanceof PdfString) map.put(key, ((PdfString) val).stringValue());
            }
        } catch(Exception x){
            LOG.error("cannot retrieve metadata from info stream", x);
        }
        try{
            if (getCatalog() != null && getCatalog().getMetadata() != null) for (final Map.Entry<String, String> entry: getCatalog().getMetadata().getPdfInfo().entrySet()){
                map.put(entry.getKey(), entry.getValue());
            }
        } catch(Exception x){
            LOG.error("cannot retrive metadata from catalog", x);

        }
        return map;
    }

    public boolean isEncrypted(){
        return containsKey("Encrypt");
    }

    public Set<Font> getFonts(int pageStart, int pageCount) throws PdfException{
        if (fonts == null){
            fonts = new HashSet<Font>();
            final Pages pages = getCatalog().getPages();
            if (pages != null){
                int from = Math.min(Math.max(pageStart, 0), pages.getPageCount());
                int to = Math.min(from + pageCount, pages.getPageCount());
                for (int i = from; i < to; i++){
                    final Page page = pages.getPage(i);
                    System.out.printf("page: %s %s%n", i, page.getContents());
                    final PdfDictionary resources = page.getResources();
                    if (resources != null){
                        final PdfDictionary fontResource = resources.getd("Font");
                        if (fontResource != null) for (final String key: fontResource.keySet()){
                            final Font font = (Font) fontResource.getd(key);
                            fonts.add(font);
                        }
                    }
                }
            }
        }
        return fonts;
    }

    public String toString() {
        try{
            return String.format("[%s, %s objects, %s pages, encrypted:%s]", get("Version"), getSize(), getCatalog().getPages().getPageCount(), isEncrypted());
        } catch(PdfException x){
            return x.getMessage();
        }
    }
}
