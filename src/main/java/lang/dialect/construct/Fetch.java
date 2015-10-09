package lang.dialect.construct;

import lang.dialect.Construct;
import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.Interpreter;
import lang.dialect.terminal.Array;
import lang.dialect.terminal.Context;
import lang.dialect.terminal.Numeral;
import lang.dialect.terminal.Symbol;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 3:34:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Fetch extends Construct {
    private Expression selector;
    private Expression composite;

    public Fetch(Object tag, Expression selector, Expression composite) {
        super(tag);
        this.selector = selector;
        this.composite = composite;
    }

    public Expression getSelector() {
        return selector;
    }

    public Expression getComposite() {
        return composite;
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        Expression sel = interpreter.evaluate(selector);
        if (sel.isSymbol()){
            Symbol s = sel.toSymbol();
            Context ctx = interpreter.evaluate(composite).toContext();
            if (ctx == Context.ENVIRONMENT) ctx = interpreter.getEnvironment();
            while (ctx.getPrevious() != null && !ctx.isBound(s)) ctx = ctx.getPrevious();
            interpreter.substitute(ctx.lookup(s));
        } else{
            Array ar = interpreter.evaluate(composite).toArray();
            Numeral in = sel.toNumeral();
            interpreter.substitute(ar.get((int)in.longValue()));
        }
        return false;
    }

    public Expression eval(int level, Context environment) throws DialectException {
        Expression sel = selector.eval(level + 1, environment);
        if (sel.isSymbol()){
            Symbol s = sel.toSymbol();
            Context ctx = composite.eval(level + 1, environment).toContext();
            if (ctx == Context.ENVIRONMENT) ctx = environment;
            while (ctx.getPrevious() != null && !ctx.isBound(s)) ctx = ctx.getPrevious();
            return ctx.lookup(s);
        } else{
            Array ar = composite.eval(level + 1, environment).toArray();
            Numeral in = sel.toNumeral();
            return ar.get((int)in.longValue());
        }
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        if (selector.isSymbol()){
            if (composite != Context.ENVIRONMENT){
                composite.unparse(writer, false);
                writer.write('.');
            }
            selector.unparse(writer, false);
        } else{
            composite.unparse(writer, false);
            writer.write('[');
            selector.unparse(writer, false);
            writer.write(']');
        }
        if (statement) writer.write(';');
    }
}
