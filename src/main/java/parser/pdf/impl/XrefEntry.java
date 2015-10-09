package parser.pdf.impl;

public abstract class XrefEntry {
    protected long a;
    protected int b;

    protected XrefEntry(long a, int b) {
        this.a = a;
        this.b = b;
    }
}
