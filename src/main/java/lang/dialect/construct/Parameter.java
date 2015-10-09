package lang.dialect.construct;

import lang.dialect.Construct;
import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.Interpreter;
import lang.dialect.terminal.Context;
import lang.dialect.terminal.Literal;
import lang.dialect.terminal.Symbol;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 29, 2004
 * Time: 11:28:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class Parameter extends Construct {
    private List<Symbol> symbols;
    private List<Expression> values;

    public Parameter(Object tag, List<Symbol> symbols, List<Expression> values) {
        super(tag);
        this.symbols = symbols;
        this.values = values;
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        Iterator<Symbol> i = symbols.iterator();
        Iterator<Expression> j = values.iterator();
        while (i.hasNext() && j.hasNext()){
            Expression value = interpreter.evaluate(j.next());
            interpreter.getEnvironment().bind(i.next(), value);
        }
        return false;
    }

    public Expression eval(int level, Context environment) throws DialectException {
        Iterator<Symbol> i = symbols.iterator();
        Iterator<Expression> j = values.iterator();
        while (i.hasNext() && j.hasNext()){
            environment.bind(i.next(), j.next().eval(level + 1, environment));
        }
        return Literal.UNDEFINED;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        Iterator<Symbol> i = symbols.iterator();
        Iterator<Expression> j = values.iterator();
        boolean comma = false;
        while (i.hasNext() && j.hasNext()){
            if (comma) writer.write(',');
            i.next().unparse(writer, false);
            Expression value = j.next();
            if (j != Literal.UNDEFINED){
                writer.write('=');
                value.unparse(writer, false);
            }
            comma = true;
        }
    }

}
