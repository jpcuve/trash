package lang.dialect.terminal;

import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.construct.Parameter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 4:32:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class Functor extends Expression implements Function {
    private List<Symbol> parameters;
    private Expression body;

    public Functor(List<Symbol> parameters, Expression body) {
        super();
        this.parameters = parameters;
        this.body = body;
    }

    public List<Symbol> getParameters() {
        return parameters;
    }

    public Expression getBody() {
        return body;
    }

    public Expression apply(List<Expression> arguments, int level, Context environment) throws DialectException {
        if (parameters.size() != arguments.size()) throw new DialectException("incorrect number of arguments");
        Parameter parameter = new Parameter(getClass(), parameters, arguments);
        Context ctx = new Context(true, environment);
        parameter.eval(level, ctx);
        return body.eval(level, ctx);
    }

    public boolean isFunction() {
        return true;
    }

    public boolean isTerminal() {
        return true;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write('@');
        writer.write('(');
        boolean comma = false;
        for (Symbol parameter : parameters){
            if (comma) writer.write(',');
            parameter.unparse(writer, false);
            comma = true;
        }
        writer.write(')');
        body.unparse(writer, false);
        if (statement) writer.write(';');
    }

}
