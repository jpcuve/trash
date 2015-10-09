package lang.ql;

import java.io.IOException;
import java.io.Writer;

/**
 * @author jpc
 */
public class Factor extends Expression {
    private String descriptor;
    private Expression factor;

    public Factor(final String descriptor, Expression factor) {
        this.descriptor = descriptor;
        this.factor = factor;
    }

    public void unparse(Writer writer) throws IOException {
        writer.write('(');
        if (descriptor != null) writer.write(descriptor);
        factor.unparse(writer);
        writer.write(')');
    }
}
