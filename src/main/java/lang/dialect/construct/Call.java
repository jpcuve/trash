package lang.dialect.construct;

import lang.dialect.Construct;
import lang.dialect.DialectException;
import lang.dialect.Expression;
import lang.dialect.Interpreter;
import lang.dialect.terminal.Context;
import lang.dialect.terminal.Function;
import lang.dialect.terminal.Functor;
import lang.dialect.terminal.Symbol;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 12:14:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class Call extends Construct {
    private Fetch variable;
    private List<Expression> arguments;
    private boolean infix;

    public Call(Object tag, Fetch operation, List<Expression> arguments) {
        super(tag);
        this.variable = operation;
        this.arguments = arguments;
        this.infix = false;
    }

    public Call(Object tag, String op, Expression arg1){
        super(tag);
        this.variable = new Fetch(tag, new Symbol(op), Context.ENVIRONMENT);
        this.arguments = new ArrayList<Expression>(1);
        arguments.add(arg1);
        this.infix = true;
    }

    public Call(Object tag, String op, Expression arg1, Expression arg2){
        super(tag);
        this.variable = new Fetch(tag, new Symbol(op), Context.ENVIRONMENT);
        this.arguments = new ArrayList<Expression>(2);
        arguments.add(arg1);
        arguments.add(arg2);
        this.infix = true;
    }

    public Fetch getVariable() {
        return variable;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public boolean reduce(Interpreter interpreter) throws DialectException {
        Function op = interpreter.evaluate(variable).toFunction();
        List<Expression> evaluatedArgs = new ArrayList<Expression>();
        for (Expression statement : arguments) evaluatedArgs.add(interpreter.evaluate(statement));
        if (((Expression)op).isFunctor()){
            Functor functor = (Functor)op;
            if (evaluatedArgs.size() != evaluatedArgs.size()) throw new DialectException("incorrect number of arguments");
            Parameter parameter = new Parameter(tag, functor.getParameters(), evaluatedArgs);
            Context ctx = new Context(true, interpreter.getEnvironment());
            interpreter.evaluate(parameter, ctx);
            interpreter.substitute(functor.getBody(), ctx);
            return true;
        } else{
            interpreter.substitute(op.apply(evaluatedArgs, 0, interpreter.getEnvironment()));
            return false;
        }
    }

    public Expression eval(int level, Context environment) throws DialectException {
        Function op = variable.eval(level + 1, environment).toFunction();
        List<Expression> evaluatedArgs = new ArrayList<Expression>();
        for (Expression statement : arguments) evaluatedArgs.add(statement.eval(level + 1, environment));
        return op.apply(evaluatedArgs, level + 1, environment);
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        if (infix){
            // writer.put('(');
            Iterator<Expression> i = arguments.iterator();
            if (arguments.size() == 1){
                variable.unparse(writer, false);
                i.next().unparse(writer, false);
            } else{
                i.next().unparse(writer, false);
                variable.unparse(writer, false);
                i.next().unparse(writer, false);
            }
            // writer.put(')');
        } else{
            variable.unparse(writer, false);
            writer.write('(');
            boolean comma = false;
            for (Expression st : arguments){
                if (comma) writer.write(',');
                st.unparse(writer, false);
                comma = true;
            }
            writer.write(')');
        }
        if (statement) writer.write(';');
    }

}
