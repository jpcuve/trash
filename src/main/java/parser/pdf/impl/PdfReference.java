package parser.pdf.impl;

public class PdfReference {
    private int id;
    private int generation;

    public PdfReference(final String s) {
        int space = s.indexOf(' ');
        if (s.endsWith(" R") && space != -1) try{
            id = Integer.parseInt(s.substring(0, space));
            generation = Integer.parseInt(s.substring(space + 1, s.length() - 2));
        } catch(NumberFormatException x){
            id = generation = -1;
        }
    }

    public int getId() {
        return id;
    }

    public int getGeneration() {
        return generation;
    }

    public String toString() {
        return String.format("%s %s R", id, generation);
    }

    public int hashCode() {
        int result;
        result = id;
        result = 31 * result + generation;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PdfReference)) return false;
        final PdfReference that = (PdfReference) obj;
        return id == that.id && generation == that.generation;
    }
}
