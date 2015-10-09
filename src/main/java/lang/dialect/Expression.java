package lang.dialect;

import lang.dialect.terminal.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;


/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 11:18:51 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Expression {
    private static final Logger LOGGER = LoggerFactory.getLogger(Expression.class);
    public Object getTag(){
        return "literal";
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        return false;
    }

    public Expression eval(Context environment) throws DialectException {
        return eval(0, environment);
    }

    public Expression eval(int level, Context environment) throws DialectException {
        return this;
    }

    public boolean isArray() { return false; }
    public boolean isContext() { return false; }
    public boolean isFault() { return false; }
    public boolean isFunction() { return false; }
    public boolean isFunctor() { return false; }
    public boolean isPrimitive() { return false; }
    public boolean isLiteral() { return false; }
    public boolean isLogical() { return false; }
    public boolean isNumeral() { return false; }
    public boolean isSymbol() { return false; }
    public boolean isTextual() { return false; }
    public boolean isConstruct() { return false; }

    public Array toArray() throws DialectException {
        if (isArray()) return (Array)this;
        throw new DialectException("not an array: " + this);
    }

    public Context toContext() throws DialectException {
        if (isContext()) return (Context)this;
        throw new DialectException("not a context: " + this);
    }

    public Function toFunction() throws DialectException {
        if (isFunction()) return (Function)this;
        throw new DialectException("not a function: " + this);
    }

    public Literal toLiteral() throws DialectException {
        if (isLiteral()) return (Literal)this;
        throw new DialectException("not a literal: " + this);
    }

    public Logical toLogical() throws DialectException {
        if (isLogical()) return (Logical)this;
        throw new DialectException("not a logical: " + this);
    }

    public Numeral toNumeral() throws DialectException {
        if (isNumeral()) return (Numeral)this;
        throw new DialectException("not a numeral: " + this);
    }

    public Symbol toSymbol() throws DialectException {
        if (isSymbol()) return (Symbol)this;
        throw new DialectException("not a symbol: " + this);
    }

    public Construct toConstruct() throws DialectException {
        if (isConstruct()) return (Construct)this;
        throw new DialectException("not a construct: " + this);
    }

    public String stringValue(){
        return toString();
    }

    public abstract void unparse(Writer writer, boolean statement) throws IOException;

    public String toStatement() {
        StringWriter stringWriter = new StringWriter();
        try{
            unparse(stringWriter, true);
        } catch(IOException x){
            LOGGER.error("cannot unparse", x);
        }
        return stringWriter.toString();
    }

    public String toString(){
        StringWriter stringWriter = new StringWriter();
        try{
            unparse(stringWriter, false);
        } catch(IOException x){
            LOGGER.error("cannot unparse", x);
        }
        return stringWriter.toString();

    }



}
