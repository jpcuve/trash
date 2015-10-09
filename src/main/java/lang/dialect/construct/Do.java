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
 * Date: Apr 28, 2004
 * Time: 6:39:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Do extends Construct {
    private Parameter doInit;
    private Expression doBody;

    public Do(Object tag, Parameter doInit, Expression doBody) {
        super(tag);
        this.doInit = doInit;
        this.doBody = doBody;
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        Context ctx = new Context(true, interpreter.getEnvironment());
        interpreter.evaluate(doInit, ctx);
        interpreter.substitute(doBody, ctx);
        return true;
    }

    public Expression eval(int level, Context environment) throws DialectException {
        Context local = new Context(true, environment);
        doInit.eval(level + 1, local);
        return doBody.eval(level + 1, local);
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write("do");
        writer.write('(');
        doInit.unparse(writer, false);
        writer.write(')');
        doBody.unparse(writer, true);
    }

}
