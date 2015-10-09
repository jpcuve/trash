package lang.ql;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author jpc
 */
public abstract class Expression {
    private String descriptor;

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public abstract void unparse(final Writer writer) throws IOException;

    public String toString() {
        final StringWriter stringWriter = new StringWriter();
        try{
            unparse(stringWriter);
        } catch(IOException x){
            // ignore
        }
        return stringWriter.toString();
    }
}
