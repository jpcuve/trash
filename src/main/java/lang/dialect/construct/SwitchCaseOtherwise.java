package lang.dialect.construct;

import lang.dialect.Construct;
import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.Interpreter;
import lang.dialect.terminal.Context;
import lang.dialect.terminal.Literal;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 28, 2004
 * Time: 6:41:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class SwitchCaseOtherwise extends Construct {
    private Expression key;
    private LinkedHashMap<List<Expression>, Expression> map;
    public static final List<Expression> EMPTY = new ArrayList<Expression>();

    public SwitchCaseOtherwise(Object tag, Expression key) {
        super(tag);
        this.key = key;
        this.map = new LinkedHashMap<List<Expression>, Expression>();
    }

    public void addCase(List<Expression> literals, Expression body){
        map.put(literals, body);
    }

    public void addCase(Expression body){
        map.put(EMPTY, body);
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        Expression k = interpreter.evaluate(key);
        for (Map.Entry<List<Expression>, Expression> entry : map.entrySet()){
            List<Expression> literals = entry.getKey();
            if (literals != EMPTY){
                for (Expression j : literals) if (k.equals(j)){
                    interpreter.substitute(entry.getValue());
                    return true;
                }
            } else{
                interpreter.substitute(entry.getValue());
                return true;
            }
        }
        interpreter.substitute(Literal.UNDEFINED);
        return false;
    }

    public Expression eval(int level, Context environment) throws DialectException {
        Expression k = key.eval(level + 1, environment);
        for (Map.Entry<List<Expression>, Expression> entry : map.entrySet()){
            List<Expression> literals = entry.getKey();
            if (literals != EMPTY){
                for (Expression j : literals) if (k.equals(j)) return entry.getValue().eval(level + 1, environment);
            } else{
                return entry.getValue().eval(level + 1, environment);
            }
        }
        return Literal.UNDEFINED;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write("switch");
        writer.write('(');
        key.unparse(writer, false);
        writer.write(')');
        writer.write('{');
        for (Map.Entry<List<Expression>, Expression> entry : map.entrySet()){
            List<Expression> literals = entry.getKey();
            if (literals != EMPTY){
                boolean comma = false;
                for (Expression literal : literals){
                    if (comma) writer.write(',');
                    literal.unparse(writer, false);
                    comma = true;
                }
            } else{
                writer.write("otherwise");
            }
            writer.write(':');
            entry.getValue().unparse(writer, true);
        }
        writer.write('}');
    }

}
