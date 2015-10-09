package lang.ql;

import java.io.IOException;
import java.io.Writer;

/**
 * @author jpc
 */
public class Literal extends Expression {
    private Object value;

    public Literal(final boolean b){
        this.value = b;
    }

    public Literal(final String s){
        this.value = s;
    }

    public Literal(final Number n){
        this.value = n;
    }

    public boolean isBoolean(){
        return value != null && value instanceof Boolean;
    }

    public boolean isString(){
        return value != null && value instanceof String;
    }

    public boolean isNumber(){
        return value != null && value instanceof Number;
    }

    public void unparse(Writer writer) throws IOException {
        if (isBoolean()) writer.write((Boolean)value ? "#t" : "#f");
        else if (isString()) writer.write('\"' + Tokenizer.escape(value.toString()) + '\"');
        else writer.write(value.toString());
    }
}
