package lang.sim6502.construct;

import lang.sim6502.Expression;
import lang.sim6502.terminal.Symbol;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 14, 2004
 * Time: 5:55:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Line extends Expression {
    private static final int TAB_SIZE = 8;
    private Symbol label = null;
    private Symbol opcode = null;
    private Expression argument = null;
    private String comment = null;

    public Symbol getLabel() {
        return label;
    }

    public void setLabel(Symbol label) {
        this.label = label;
    }

    public Symbol getOpcode() {
        return opcode;
    }

    public void setOpcode(Symbol opcode) {
        this.opcode = opcode;
    }

    public Expression getArgument() {
        return argument;
    }

    public void setArgument(Expression argument) {
        this.argument = argument;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void unparse(Writer writer) throws IOException {
        if (label != null){
            label.unparse(writer);
            writer.write(':');
        }
        writer.write('\t');
        if (opcode != null){
            opcode.unparse(writer);
        }
        writer.write('\t');
        if (argument != null){
            writer.write(' ');
            argument.unparse(writer);
        }
        if (comment != null){
            writer.write('\t');
            writer.write(comment);
        }
    }
}
