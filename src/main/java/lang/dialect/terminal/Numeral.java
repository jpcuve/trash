package lang.dialect.terminal;

import lang.dialect.Expression;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 27, 2004
 * Time: 2:22:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Numeral extends Expression {
    private String literal;
    private boolean integer;

    public static final Numeral ZERO = new Numeral(0);
    public static final Numeral ONE = new Numeral(1);

    public Numeral(String s, boolean integer){
        this.literal = s;
        this.integer = integer;
    }

    public Numeral(long l){
        this.literal = Long.toString(l);
        this.integer = true;
    }

    public Numeral(double d){
        literal = Double.toString(d);
        this.integer = false;
    }

    public boolean isInteger() {
        return integer;
    }

    public long longValue(){
        return Long.parseLong(literal);
    }

    public double doubleValue(){
        return Double.parseDouble(literal);
    }

    public String stringValue(){
        return literal;
    }

    public boolean isNumeral() {
        return true;
    }

    public boolean isTerminal() {
        return true;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write(literal);
        if (statement) writer.write(';');
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Numeral)) return false;
        Numeral numeral = (Numeral)obj;
        return literal.equalsIgnoreCase(numeral.literal);
    }

    public int hashCode() {
        return literal.hashCode();
    }
}
