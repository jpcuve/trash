package parser.pdf;

import java.util.List;

public interface PageNode {
    PageNode getParent() throws PdfException ;
    List<PageNode> getChildNodes() throws PdfException;
    int getPageCount() throws PdfException;
    Page getPage(int i) throws PdfException;
}
