package lang.sim6502.construct;

import lang.sim6502.Expression;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: May 2, 2005
 * Time: 7:45:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class Call extends Expression {
    private String operator;
    private List<Expression> arguments;
    private boolean infix;

    public Call(String operator, Expression argument){
        this.operator = operator;
        this.arguments = new ArrayList<Expression>(1);
        arguments.add(argument);
        this.infix = true;
    }

    public Call(String operator, Expression argument1, Expression argument2){
        this.operator = operator;
        this.arguments = new ArrayList<Expression>(2);
        arguments.add(argument1);
        arguments.add(argument2);
        this.infix = true;
    }

    public void unparse(Writer writer) throws IOException {
        if (infix){
            Iterator<Expression> i = arguments.iterator();
            if (arguments.size() == 1){
                writer.write(operator);
                i.next().unparse(writer);
            } else{
                i.next().unparse(writer);
                writer.write(operator);
                i.next().unparse(writer);
            }
        } else{
            writer.write(operator);
            writer.write('(');
            boolean comma = false;
            for (Expression st : arguments){
                if (comma) writer.write(',');
                st.unparse(writer);
                comma = true;
            }
            writer.write(')');
        }

    }
}
