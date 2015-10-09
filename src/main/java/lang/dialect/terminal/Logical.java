package lang.dialect.terminal;

import lang.dialect.Expression;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 27, 2004
 * Time: 2:27:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Logical extends Expression {
    private boolean literal;

    public static final Logical TRUE = new Logical(true);
    public static final Logical FALSE = new Logical(false);

    private Logical(boolean b) {
        this.literal = b;
    }

    public boolean booleanValue(){
        return literal;
    }

    public boolean isLogical() {
        return true;
    }

    public boolean isTerminal() {
        return true;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write('#');
        writer.write(literal ? 't' : 'f');
        if (statement) writer.write(';');
    }
}
