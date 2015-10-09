package lang.dialect.construct;

import lang.dialect.Construct;
import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.Interpreter;
import lang.dialect.terminal.Context;
import lang.dialect.terminal.Logical;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 28, 2004
 * Time: 6:42:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Repeat extends Construct {
    private Expression body;
    private Expression test;

    public Repeat(Object tag, Expression body, Expression test) {
        super(tag);
        this.body = body;
        this.test = test;
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        Expression ret;
        do{
            ret = interpreter.evaluate(body);
        } while(!(interpreter.evaluate(test) == Logical.TRUE));
        interpreter.substitute(ret);
        return false;
    }

    public Expression eval(int level, Context environment) throws DialectException {
        Expression ret;
        do{
            ret = body.eval(level + 1, environment);
        } while(!(test.eval(level + 1, environment) == Logical.TRUE));
        return ret;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write("repeat");
        writer.write(' ');
        body.unparse(writer, true);
        writer.write("until");
        writer.write('(');
        test.unparse(writer, false);
        writer.write(')');
    }

}
