package lang.sim6502.construct;

import lang.sim6502.Expression;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 14, 2004
 * Time: 5:55:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Block extends Expression {
    private List<Expression> lines = new ArrayList<Expression>();

    public Block(List<Expression> lines) {
        this.lines = lines;
    }

    public List<Expression> getLines() {
        return lines;
    }

    public void unparse(Writer writer) throws IOException {
        for (Expression line: lines){
            line.unparse(writer);
            writer.write('\n');
        }
    }
}
