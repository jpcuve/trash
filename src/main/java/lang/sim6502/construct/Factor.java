package lang.sim6502.construct;

import lang.sim6502.Expression;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: May 2, 2005
 * Time: 9:24:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class Factor extends Expression {
    private Expression factor;

    public Factor(Expression factor) {
        this.factor = factor;
    }

    public void unparse(Writer writer) throws IOException {
        writer.write('(');
        factor.unparse(writer);
        writer.write(')');
    }
}
