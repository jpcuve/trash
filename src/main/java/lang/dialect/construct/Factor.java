package lang.dialect.construct;

import lang.dialect.Construct;
import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.Interpreter;
import lang.dialect.terminal.Context;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: May 10, 2004
 * Time: 4:13:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Factor extends Construct {
    private Expression factor;

    public Factor(Object tag, Expression factor) {
        super(tag);
        this.factor = factor;
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        interpreter.substitute(factor);
        return true;
    }

    public Expression eval(int level, Context environment) throws DialectException {
        return factor.eval(level, environment);
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write('(');
        factor.unparse(writer, false);
        writer.write(')');
    }
}
