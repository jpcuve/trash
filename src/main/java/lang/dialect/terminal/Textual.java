package lang.dialect.terminal;

import lang.dialect.Expression;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 27, 2004
 * Time: 2:23:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Textual extends Expression {
    private StringBuilder literal;

    public Textual(String s){
        literal = new StringBuilder(s);
    }

    public String stringValue(){
        return literal.toString();
    }

    public boolean isTextual() {
        return true;
    }

    public boolean isTerminal() {
        return true;
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write('"');
        for (int i = 0; i < literal.length(); i++){
            char c = literal.charAt(i);
            switch(c){
                case '"' :
                    writer.write("\\\"");
                    break;
                case '\n':
                    writer.write("\\n");
                    break;
                case '\t':
                    writer.write("\\t");
                    break;
                case '\r':
                    writer.write("\\r");
                    break;
                case '\b':
                    writer.write("\\b");
                    break;
                case '\f':
                    writer.write("\\f");
                    break;
                case '\\':
                    writer.write("\\\\");
                    break;
                default:
                    writer.write(c);
                    break;
            }
        }
        writer.write('"');
        if (statement) writer.write(';');
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Textual)) return false;
        Textual textual = (Textual)obj;
        return literal.toString().equals(textual.literal.toString());
    }

    public int hashCode() {
        return literal.toString().hashCode();
    }
}
