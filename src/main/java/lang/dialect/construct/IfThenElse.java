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
 * Date: Apr 26, 2004
 * Time: 11:16:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class IfThenElse extends Construct {
    private Expression test;
    private Expression truePath;
    private Expression falsePath;

    public IfThenElse(Object tag, Expression test, Expression truePath, Expression falsePath) {
        super(tag);
        this.test = test;
        this.truePath = truePath;
        this.falsePath = falsePath;
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        interpreter.substitute(interpreter.evaluate(test) == Logical.TRUE ? truePath : falsePath);
        return true;
    }

    public Expression eval(int level, Context environment) throws DialectException {
        return test.eval(level + 1, environment).toLogical() == Logical.TRUE ? truePath.eval(level + 1, environment) : falsePath.eval(level + 1, environment);
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write("if");
        writer.write('(');
        test.unparse(writer, false);
        writer.write(')');
        truePath.unparse(writer, true);
        if (falsePath != Literal.UNDEFINED){
            writer.write("else");
            writer.write(' ');
            falsePath.unparse(writer, true);
        }
    }

}
