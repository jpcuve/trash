package parser.pdf;

import parser.pdf.impl.PdfArray;
import parser.pdf.impl.PdfDictionary;

import java.util.ArrayList;
import java.util.List;

public class Pages extends PdfDictionary implements PageNode {
    private PageNode parent;
    private List<PageNode> children;

    public PageNode getParent() throws PdfException {
        if (parent == null && containsKey("Parent")) parent = (PageNode) getd("Parent");
        return parent;
    }

    public List<PageNode> getChildNodes() throws PdfException {
        if (children == null){
            children = new ArrayList<PageNode>();
            final PdfArray kids = geta("Kids");
            for (int i = 0; i < kids.size(); i++) children.add((PageNode) kids.getd(i));
        }
        return children;
    }

    public int getPageCount() throws PdfException{
        return geti("Count");
    }

    public Page getPage(int i) throws PdfException{
        int total = 0;
        for (final PageNode child: getChildNodes()){
            final int count = child.getPageCount();
            if (total + count > i) return child.getPage(i - total);
            total += count;
        }
        return null;
    }
}
