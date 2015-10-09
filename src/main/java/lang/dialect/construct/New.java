package lang.dialect.construct;

import lang.dialect.Construct;
import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.Interpreter;
import lang.dialect.terminal.Array;
import lang.dialect.terminal.Context;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: May 3, 2004
 * Time: 6:44:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class New extends Construct {
    private int type;
    private String custom;

    public static final int ARRAY = 0;
    public static final int CONTEXT = 1;
    public static final int CUSTOM = 2;

    public New(Object tag, int type) {
        super(tag);
        this.type = type;
    }

    public New(Object tag, int type, String custom) {
        super(tag);
        this.type = type;
        this.custom = custom;
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        switch(type){
            case ARRAY:
                interpreter.substitute(new Array(true));
                break;
            case CONTEXT:
                interpreter.substitute(new Context(true, null));
                break;
            default:
                throw new DialectException("custom objects not implemented");
        }
        return false;
    }

    public Expression eval(int level, Context environment) throws DialectException {
        switch(type){
            case ARRAY:
                return new Array(true);
            case CONTEXT:
                return new Context(true, null);
            default:
                throw new DialectException("custom objects not implemented");
        }
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write('@');
        switch(type){
            case ARRAY:
                writer.write("#()");
                break;
            case CONTEXT:
                writer.write("#[]");
                break;
            default:
                writer.write(custom);
                break;
        }
    }

}
