package parser.pdf;

import parser.pdf.impl.PdfArray;
import parser.pdf.impl.PdfDictionary;
import parser.pdf.impl.PdfStream;

import java.util.Collections;
import java.util.List;

public class Page extends PdfDictionary implements PageNode {
    private PageNode parent;
    private PdfDictionary resources;
    private PdfArray contents;

    public PageNode getParent() throws PdfException {
        if (parent == null && containsKey("Parent")) parent = (PageNode) getd("Parent");
        return parent;
    }

    public PdfDictionary getResources() throws PdfException {
        if (resources == null && containsKey("Resources")) resources = getd("Resources");
        return resources;
    }

    public PdfArray getContents() throws PdfException {
        if (contents == null){
            contents = new PdfArray();
            if (context != null){
                contents.setContext(context);
                final Object o = context.resolve(get("Contents"));
                if (o instanceof PdfStream){
                    contents.add(o);
                } else if (o instanceof PdfArray){
                    contents = ((PdfArray) o);
                }
            }
        }
        return contents;
    }

    public List<PageNode> getChildNodes() {
        return Collections.emptyList();
    }

    public int getPageCount() {
        return 1;
    }

    public Page getPage(int i) {
        return this;
    }
}
