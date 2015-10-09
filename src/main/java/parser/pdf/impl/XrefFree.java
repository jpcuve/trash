package parser.pdf.impl;

public class XrefFree extends XrefEntry {
    public XrefFree(long nextFreeObject, int nextGenerationNumber) {
        super(nextFreeObject, nextGenerationNumber);
    }

    public long getNextFreeObject(){
        return a;
    }

    public int getNextGenerationNumber(){
        return b;
    }

    public String toString() {
        return String.format("free, next:%s, next gen:%s", a, b);
    }
}
