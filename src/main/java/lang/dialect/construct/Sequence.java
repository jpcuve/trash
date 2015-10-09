package lang.dialect.construct;

import lang.dialect.Construct;
import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.Interpreter;
import lang.dialect.terminal.Context;
import lang.dialect.terminal.Literal;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 11:27:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class Sequence extends Construct {
    private List<Expression> statements;

    public Sequence(Object tag, List<Expression> statements) {
        super(tag);
        this.statements = statements;
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        Expression ret = Literal.UNDEFINED;
        for (Expression st : statements) ret = interpreter.evaluate(st);
        interpreter.substitute(ret);
        return false;
    }

    public Expression eval(int level, Context environment) throws DialectException {
        Expression ret = Literal.UNDEFINED;
        for (Expression st : statements) ret = st.eval(level + 1, environment);
        return ret;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write('{');
        for (Expression st : statements) st.unparse(writer, true);
        writer.write('}');
    }
}
