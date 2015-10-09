package lang.dialect.construct;

import lang.dialect.Construct;
import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.Interpreter;
import lang.dialect.terminal.*;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 4:02:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Assignment extends Construct {
    private Fetch variable;
    private Expression value;

    public Assignment(Object tag, Fetch variable, Expression value) {
        super(tag);
        this.variable = variable;
        this.value = value;
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        Expression sel = interpreter.evaluate(variable.getSelector());
        if (sel.isSymbol()){
            Symbol s = sel.toSymbol();
            Context ctx = interpreter.evaluate(variable.getComposite()).toContext();
            if (ctx == Context.ENVIRONMENT) ctx = interpreter.getEnvironment();
            while (ctx.getPrevious() != null && ctx.getPrevious().isMutable() && !ctx.isBound(s)) ctx = ctx.getPrevious();
            ctx.bind(s, interpreter.evaluate(value));
        } else{
            Array ar = interpreter.evaluate(variable.getComposite()).toArray();
            Numeral in = sel.toNumeral();
            ar.set((int)in.longValue(), interpreter.evaluate(value));
        }
        interpreter.substitute(Literal.UNDEFINED);
        return false;
    }

    public Expression eval(int level, Context environment) throws DialectException {
        Expression sel = variable.getSelector().eval(level + 1, environment);
        if (sel.isSymbol()){
            Symbol s = sel.toSymbol();
            Context ctx = variable.getComposite().eval(level + 1, environment).toContext();
            if (ctx == Context.ENVIRONMENT) ctx = environment;
            while (ctx.getPrevious() != null && ctx.getPrevious().isMutable() && !ctx.isBound(s)) ctx = ctx.getPrevious();
            ctx.bind(s, value.eval(level + 1, environment));
        } else{
            Array ar = variable.getComposite().eval(level + 1, environment).toArray();
            Numeral in = sel.toNumeral();
            ar.set((int)in.longValue(), value.eval(level + 1, environment));
        }
        return Literal.UNDEFINED;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        variable.unparse(writer, false);
        writer.write('=');
        value.unparse(writer, false);
        if (statement) writer.write(';');
    }

}
