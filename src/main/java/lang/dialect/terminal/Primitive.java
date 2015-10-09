package lang.dialect.terminal;

import lang.dialect.DialectException;
import lang.dialect.Expression;

import java.io.IOException;
import java.io.Writer;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 5:33:02 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Primitive extends Expression implements Function {
    private String libraryName = "";
    private String name;
    private int minNbArg = 0;
    private int maxNbArg = 0;

    public Primitive(String name, int minNbArg, int maxNbArg) {
        this.name = name;
        this.minNbArg = minNbArg;
        this.maxNbArg = maxNbArg;
    }

    public Primitive(String name, int nbArgs) {
        this.name = name;
        this.minNbArg = nbArgs;
        this.maxNbArg = nbArgs;
    }

    public Primitive(String name) {
        this.name = name;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getName() {
        return name;
    }

    public Expression apply(List<Expression> arguments, int level, Context environment) throws DialectException {
        if(arguments.size() < minNbArg || arguments.size() > maxNbArg) throw new DialectException("wrong number of arguments: " + this);
        try{
            return eval(arguments, environment);
        } catch(Exception x){
            throw new DialectException(x);
        }
    }

    public abstract Expression eval(List<Expression> args, Context environment) throws Exception;

    public boolean isFunction() {
        return true;
    }

    public boolean isTerminal() {
        return true;
    }

    public boolean isPrimitive() {
        return true;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write("@PRIMITIVE:");
        writer.write(libraryName);
        writer.write('.');
        writer.write(name);
        writer.write('@');
        if (statement) writer.write(';');
    }
}
