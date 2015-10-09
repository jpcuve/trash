package lang.ql;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jpc
 */
public class Composite extends Expression {
    private List<Expression> elements;
    private boolean range;

    public Composite(final List<Expression> elements) {
        this.elements = elements;
        this.range = false;
    }

    public Composite(final Expression from, final Expression to){
        this.elements = new ArrayList<Expression>();
        elements.add(from);
        elements.add(to);
        this.range = true;
    }

    public boolean isRange() {
        return range;
    }

    public void unparse(Writer writer) throws IOException {
        if (range){
            writer.write('[');
            elements.get(0).unparse(writer);
            writer.write(':');
            elements.get(1).unparse(writer);
            writer.write(']');
        } else{
            writer.write('{');
            boolean comma = false;
            for (final Expression element: elements){
                if (comma) writer.write(',');
                element.unparse(writer);
                comma = true;
            }
            writer.write('}');
        }
    }
}
