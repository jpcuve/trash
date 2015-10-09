package lang.dialect.terminal;

import lang.dialect.Expression;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 27, 2004
 * Time: 2:33:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Fault extends Expression {
    private Exception literal;

    public Fault(Exception e) {
        this.literal = e;
    }

    public boolean isFault() {
        return true;
    }

    public boolean isTerminal() {
        return true;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write('%');
        writer.write(literal.getMessage());
        writer.write('%');
        if (statement) writer.write(';');
    }
}
