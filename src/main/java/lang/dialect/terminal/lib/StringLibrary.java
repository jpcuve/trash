package lang.dialect.terminal.lib;

import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.terminal.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 24, 2005
 * Time: 10:11:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class StringLibrary extends Library {
    public StringLibrary() {
        super("string");
        addPrimitive(new Primitive("left", 2){
            public Expression eval(List<Expression> args, Context environment) throws DialectException {
                return new Textual(args.get(0).stringValue().substring(0, (int)args.get(1).toNumeral().longValue()));
            }
        });

    }
}
