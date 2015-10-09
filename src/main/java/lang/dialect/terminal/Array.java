package lang.dialect.terminal;

import lang.dialect.DialectException;
import lang.dialect.Expression;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 28, 2004
 * Time: 4:37:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Array extends Expression {
    private Thread creator = Thread.currentThread();
    private boolean mutable = true;
    private Expression[] elements = new Expression[0];

    public Array(boolean mutable){
        this.mutable = mutable;
    }

    public Array(List<Expression> elements) {
        this.mutable = false;
        this.elements = new Expression[elements.size()];
        elements.toArray(this.elements);
    }

    public Expression get(int index) {
        if (index >= elements.length) return Literal.UNDEFINED;
        return elements[index] == null ? Literal.NULL : elements[index];
    }

    public void set(int index, Expression e) throws DialectException {
        if (!mutable) throw new DialectException("cannot modify literal");
        if (Thread.currentThread() != creator) throw new DialectException("array modified by invalid thread");
        if (index >= elements.length){
            Expression[] es = new Expression[index + 1];
            if (elements.length > 0) System.arraycopy(elements, 0, es, 0, elements.length);
            elements = es;
        }
        elements[index] = e;
    }

    public List<Expression> elements(){
        return Arrays.asList(elements);
    }

    public boolean isArray() {
        return true;
    }

    public boolean isTerminal() {
        return true;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write('#');
        writer.write('(');
        boolean comma = false;
        for (Expression element: elements){
            if (comma) writer.write(',');
            if (element == null) writer.write('#');
            else element.unparse(writer, false);
            comma = true;
        }
        writer.write(')');
        if (statement) writer.write(';');
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("#(");
        boolean comma = false;
        for (Expression element: elements){
            if (comma) sb.append(',');
            sb.append(element == null ? "#" : element.toString());
            comma = true;
        }
        sb.append(')');
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Array)) return false;
        Array array = (Array)obj;
        if (elements.length != array.elements.length) return false;
        for (int i = 0, j = 0; i < elements.length && j < elements.length; i++, j++){
            if (elements[i] == null){
                if (elements[j] != null) return false;
            } else{
                if (!elements[i].equals(array.elements[j])) return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int result = 17;
        for (Expression element: elements){
            result = 37 * result + (element == null ? 0 : element.hashCode());
        }
        return result;
    }

    public static String getString(Array array, int index, String defaultString) {
        if (index < 0 || index >= array.elements.length || !array.elements[index].isTextual()) return defaultString;
        return array.elements[index].stringValue();
    }
}
