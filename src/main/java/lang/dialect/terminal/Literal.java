package lang.dialect.terminal;

import lang.dialect.Expression;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 11:19:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class Literal extends Expression {
    private Object literal;

    public static final Literal UNDEFINED = new Literal("?");
    public static final Literal NULL = new Literal("#");

    private Literal(Object literal) {
        this.literal = literal;
    }

    public boolean isLiteral(){
        return true;
    }

    public boolean isTerminal() {
        return true;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write(literal.toString());
        if (statement) writer.write(';');
    }
}
