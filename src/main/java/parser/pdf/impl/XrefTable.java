package parser.pdf.impl;

import java.io.IOException;
import java.io.Writer;

public interface XrefTable {
    boolean isReferencing(int id);
    XrefEntry getEntry(int id) throws IOException;
    void dump(Writer writer) throws IOException;
}
