package lang.dialect.terminal.lib;

import lang.dialect.Expression;
import lang.dialect.Parser;
import lang.dialect.terminal.*;

import java.io.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 29, 2004
 * Time: 1:34:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortLibrary extends Library {
    private Reader reader;
    private Writer writer;

    public PortLibrary() {
        super("port");
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.writer = new OutputStreamWriter(System.out);
        addPrimitive(new Primitive("put", 1){
            public Expression eval(List<Expression> args, Context environment) throws Exception {
                return new Parser(getClass(), reader).parse();
            }
        });
        addPrimitive(new Primitive("write", 1){
            public Expression eval(List<Expression> args, Context environment) throws Exception {
                writer.write((args.get(0)).toStatement());
                writer.flush();
                return Literal.UNDEFINED;
            }
        });
        addPrimitive(new Primitive("display", 0, Integer.MAX_VALUE){
            public Expression eval(List<Expression> args, Context environment) throws Exception {
                StringWriter log = new StringWriter();
                for (Expression e : args){
                    writer.write(e.stringValue());
                    log.write(e.toString());
                }
                writer.flush();
                log.flush();
                return Literal.UNDEFINED;
            }
        });
    }

}
