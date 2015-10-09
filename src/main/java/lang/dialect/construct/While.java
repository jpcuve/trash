package lang.dialect.construct;

import lang.dialect.Construct;
import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.Interpreter;
import lang.dialect.terminal.Context;
import lang.dialect.terminal.Literal;
import lang.dialect.terminal.Logical;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 28, 2004
 * Time: 6:41:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class While extends Construct {
    private Expression test;
    private Expression body;

    public While(Object tag, Expression test, Expression body) {
        super(tag);
        this.test = test;
        this.body = body;
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        Expression t = interpreter.evaluate(test);
        Expression ret = Literal.UNDEFINED;
        while (t == Logical.TRUE){
            ret = interpreter.evaluate(body);
            t = interpreter.evaluate(test);
        }
        interpreter.substitute(ret);
        return false;
    }

    public Expression eval(int level, Context environment) throws DialectException {
        Expression t = test.eval(level + 1, environment);
        Expression ret = Literal.UNDEFINED;
        while (t == Logical.TRUE){
            ret = body.eval(level + 1, environment);
            t = test.eval(level + 1, environment);
        }
        return ret;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write("while");
        writer.write('(');
        test.unparse(writer, false);
        writer.write(')');
        body.unparse(writer, true);
    }

}
