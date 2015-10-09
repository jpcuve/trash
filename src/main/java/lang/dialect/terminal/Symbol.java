package lang.dialect.terminal;

import lang.dialect.Expression;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 28, 2004
 * Time: 5:24:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class Symbol extends Expression {
    private String literal;

    public Symbol(String literal) {
        this.literal = literal;
    }

    public boolean isSymbol(){
        return true;
    }

    public boolean isTerminal() {
        return true;
    }

    public String stringValue(){
        return literal;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write(literal);
        if (statement) writer.write(';');
    }
}
