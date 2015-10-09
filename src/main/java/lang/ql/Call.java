package lang.ql;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jpc
 */
public class Call extends Expression {
    private String method;
    private List<Expression> arguments;
    private boolean keyword;

    public Call(final String method, final List<Expression> arguments) {
        this.method = method;
        this.arguments = arguments;
        this.keyword = false;
    }

    public Call(final Keyword keyword, final List<Expression> arguments) {
        this.method = keyword.toString();
        this.arguments = arguments;
        this.keyword = true;
    }

    public Call(final Keyword keyword, final Expression arg){
        this.method = keyword.toString();
        this.arguments = new ArrayList<Expression>(1);
        arguments.add(arg);
        this.keyword = true;
    }

    public Call(final Keyword keyword, final Expression arg1, final Expression arg2){
        this.method = keyword.toString();
        this.arguments = new ArrayList<Expression>(2);
        arguments.add(arg1);
        arguments.add(arg2);
        this.keyword = true;
    }

    public void unparse(Writer writer) throws IOException {
        if (keyword){
//            writer.write('(');
            if (arguments.size() == 1){
                writer.write(method);
                arguments.get(0).unparse(writer);
            } else{
                boolean op = false;
                for (final Expression arg: arguments){
                    if (op) writer.write(method);
                    arg.unparse(writer);
                    op = true;
                }
            }
//            writer.write(')');
        } else{
            writer.write(method);
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
