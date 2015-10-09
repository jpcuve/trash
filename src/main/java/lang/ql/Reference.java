package lang.ql;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * @author jpc
 */
public class Reference extends Expression {
    private String table;
    private Expression id;
    private List<String> fields;

    public Reference(final String table, final Expression id, final List<String> fields) {
        this.table = table;
        this.id = id;
        this.fields = fields;
    }

    public void unparse(Writer writer) throws IOException {
        writer.write(table);
        writer.write(':');
        id.unparse(writer);
        for (final String field: fields){
            writer.write('.');
            writer.write(field);
        }
    }
}
