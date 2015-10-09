package parser.pdf.impl;

import parser.pdf.PdfException;

import java.util.Collection;
import java.util.HashMap;

public class PdfDictionary extends HashMap<String, Object> implements Container {
    protected Context context;
    private long position;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public Collection<Object> getChildren() {
        return values();
    }

    public PdfStream getst(final String key) throws PdfException {
        final Object o = get(key);
        return context == null ? (o instanceof PdfStream ? (PdfStream) o : null) : context.evalst(o);
    }

    public PdfDictionary getd(final String key) throws PdfException {
        final Object o = get(key);
        return context == null ? (o instanceof PdfDictionary ? (PdfDictionary) o : null) : context.evald(o);
    }

    public PdfArray geta(final String key) throws PdfException {
        final Object o = get(key);
        return context == null ? (o instanceof PdfArray ? (PdfArray) o : null) : context.evala(o);
    }

    public int geti(final String key) throws PdfException {
        final Object o = get(key);
        return context == null ? (o instanceof Number ? ((Number) o).intValue() : 0) : context.evali(o);
    }

    public long getl(final String key) throws PdfException {
        final Object o = get(key);
        return context == null ? (o instanceof Number ? ((Number) o).longValue() : 0) : context.evall(o);
    }

    public boolean getb(final String key) throws PdfException {
        final Object o = get(key);
        return context == null ? (o instanceof Boolean ? (Boolean) o : false) : context.evalb(o);
    }

    public String getn(final String key) throws PdfException {
        final Object o = get(key);
        return context == null ? (o instanceof String ? (String) o : null) : context.evaln(o);
    }

    public PdfString gets(final String key) throws PdfException {
        final Object o = get(key);
        return context == null ? (o instanceof PdfString ? (PdfString) o : null) : context.evals(o);
    }

    public String toPrettyString(){
        return toString(0);
    }

    public String toString(int prettyPrint){
        final StringBuilder sb = new StringBuilder();
//        sb.append(String.format("[Ox%generationDate]", position));
        sb.append("<<");
        boolean space = false;
        for (final Entry<String, Object> entry: entrySet()){
            if (space) sb.append(' ');
            final StringBuffer kp = new StringBuffer();
            kp.append(Tokenizer.toString(entry.getKey())).append(' ').append(Tokenizer.toString(entry.getValue(), prettyPrint < 0 ? -1 : prettyPrint + 1));
            Tokenizer.appendLine(sb, prettyPrint, kp.toString());
            space = true;
        }
        Tokenizer.appendLine(sb, prettyPrint, ">>");
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(-1);
    }
}
