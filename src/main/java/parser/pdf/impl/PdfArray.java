package parser.pdf.impl;

import parser.pdf.PdfException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PdfArray extends ArrayList<Object> implements Container {
    protected Context context;
    protected long position;

    public List<Integer> extractIntegers(){
        final List<Integer> integers = new ArrayList<Integer>();
        for (Object o: this) try {
            if (context != null) o = context.resolve(o);
            if (o instanceof Number) integers.add(((Number) o).intValue());
        } catch(PdfException x){
            // ignore
        }
        return integers;
    }


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
        return this;
    }

    public int geti(int index) throws PdfException {
        final Object o = get(index);
        return context == null ? (o instanceof Number ? ((Number) o).intValue() : 0) : context.evali(o);
    }

    public String getn(int index) throws PdfException {
        final Object o = get(index);
        return context == null ? (o instanceof String ? (String) o : null) : context.evaln(o);
    }

    public PdfString gets(int index) throws PdfException {
        final Object o = get(index);
        return context == null ? (o instanceof PdfString ? (PdfString) o : null) : context.evals(o);
    }

    public PdfDictionary getd(int index) throws PdfException {
        final Object o = get(index);
        return context == null ? (o instanceof PdfDictionary ? (PdfDictionary) o : null) : context.evald(o);
    }

    public PdfStream getst(int index) throws PdfException {
        final Object o = get(index);
        return context == null ? (o instanceof PdfStream ? (PdfStream) o : null) : context.evalst(o);
    }

    public String toString(final int prettyPrint){
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean space = false;
        for (final Object element: this){
            if (space) sb.append(' ');
            Tokenizer.appendLine(sb, prettyPrint, Tokenizer.toString(element, prettyPrint < 0 ? - 1 : prettyPrint + 1));
            space = true;
        }
        Tokenizer.appendLine(sb, prettyPrint, "]");
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (final Object o: this) sb.append(Tokenizer.toString(o)).append(' ');
        sb.append(']');
        return sb.toString();
    }
}
