package parser.pdf.impl;

public class XrefInUse extends XrefEntry {
    public XrefInUse(long offset, int generationNumber) {
        super(offset, generationNumber);
    }

    public long getOffset(){
        return a;
    }

    public int getGenerationNumber(){
        return b;
    }

    public String toString() {
        return String.format("in use, offset: 0x%012x, gen: %s", a, b);
    }
}
