package parser.pdf.impl;

public class XrefCompressedObject extends XrefEntry {
    public XrefCompressedObject(long objectStream, int index) {
        super(objectStream, index);
    }

    public long getObjectStream(){
        return a;
    }

    public int getIndex(){
        return b;
    }

    public String toString() {
        return String.format("comp obj, stream: %s, index: %s", a, b);
    }
}
