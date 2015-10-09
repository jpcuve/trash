package lang.dialect.terminal;

import java.io.IOException;
import java.io.Writer;


/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 26, 2004
 * Time: 5:32:24 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Library extends Context {
    private String name;

    protected Library(String name) {
        super(false, null);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addPrimitive(Primitive primitive){
        primitive.setLibraryName(name);
        bindings.put(primitive.getName(), primitive);
    }

    public void unparse(Writer writer, boolean statement) throws IOException {
        writer.write("@LIBRARY:");
        writer.write(name);
        writer.write('@');
        if (statement) writer.write(';');
    }
}
