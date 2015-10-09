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
 * Time: 6:42:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class For extends Construct {
    private Parameter init;
    private Expression test;
    private Parameter next;
    private Expression body;

    public For(Object tag, Parameter init, Expression test, Parameter next, Expression body) {
        super(tag);
        this.init = init;
        this.test = test;
        this.next = next;
        this.body = body;
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        interpreter.evaluate(init);
        Expression t = interpreter.evaluate(test);
        Expression ret = Literal.UNDEFINED;
        while (t == Logical.TRUE){
            ret = interpreter.evaluate(body);
            interpreter.evaluate(next);
            t = interpreter.evaluate(test);
        }
        interpreter.substitute(ret);
        return false;
    }

    public Expression eval(int level, Context environment) throws DialectException {
        init.eval(level + 1, environment);
        Expression t = test.eval(level + 1, environment);
        Expression ret = Literal.UNDEFINED;
        while (t == Logical.TRUE){
            ret = body.eval(level + 1, environment);
            next.eval(level + 1, environment);
            t = test.eval(level + 1, environment);
        }
        return ret;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write("for");
        writer.write('(');
        init.unparse(writer, false);
        writer.write(';');
        test.unparse(writer, false);
        writer.write(';');
        next.unparse(writer, false);
        writer.write(')');
        body.unparse(writer, true);
    }

}
