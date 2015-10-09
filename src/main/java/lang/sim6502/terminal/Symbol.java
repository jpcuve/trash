package lang.sim6502.terminal;

import lang.sim6502.Expression;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 14, 2004
 * Time: 5:59:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Symbol extends Expression {
    
    private String literal;

    public Symbol(String literal) {
        this.literal = literal;
    }

    public void unparse(Writer writer) throws IOException {
        writer.write(literal);
    }

    public String toString() {
        return literal;
    }
}
