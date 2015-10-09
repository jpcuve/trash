package lang.dialect.construct;

import lang.dialect.Construct;
import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.Interpreter;
import lang.dialect.terminal.Context;
import lang.dialect.terminal.Fault;
import lang.dialect.terminal.Symbol;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 28, 2004
 * Time: 6:42:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class TryCatch extends Construct {
    private Expression tryBody;
    private Symbol fault;
    private Expression catchBody;

    public TryCatch(Object tag, Expression tryBody, Symbol fault, Expression catchBody) {
        super(tag);
        this.tryBody = tryBody;
        this.fault = fault;
        this.catchBody = catchBody;
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        try{
            interpreter.substitute(interpreter.evaluate(tryBody));
            return false;
        } catch(Exception x){
            interpreter.pop();
            Context ctx = new Context(true, interpreter.getEnvironment());
            ctx.bind(fault, new Fault(x));
            interpreter.substitute(catchBody, ctx);
            return true;
        }
    }

    public Expression eval(int level, Context environment) throws DialectException {
        try{
            return tryBody.eval(level + 1, environment);
        } catch(Exception x){
            Context ctx = new Context(true, environment);
            ctx.bind(fault, new Fault(x));
            return catchBody.eval(level + 1, ctx);
        }
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write("try");
        writer.write(' ');
        tryBody.unparse(writer, true);
        writer.write("catch");
        writer.write('(');
        fault.unparse(writer, false);
        writer.write(')');
        catchBody.unparse(writer, true);
    }

}
