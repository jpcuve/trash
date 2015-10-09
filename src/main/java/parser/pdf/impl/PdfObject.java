package parser.pdf.impl;

public class PdfObject {
    private int id;
    private int generation;
    private Object content;

    public PdfObject(final String s) {
        int space = s.indexOf(' ');
        if (s.endsWith(" obj") && space != -1) try{
            id = Integer.parseInt(s.substring(0, space));
            generation = Integer.parseInt(s.substring(space + 1, s.length() - 4));
        } catch(NumberFormatException x){
            id = generation = -1;
        }
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(id).append(' ').append(generation).append(" obj ");
        sb.append(Tokenizer.toString(content));
        sb.append(" endobj");
        return sb.toString();
    }
}
