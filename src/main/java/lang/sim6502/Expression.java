package lang.sim6502;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 14, 2004
 * Time: 5:57:00 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Expression {
    public abstract void unparse(Writer writer) throws IOException;

    public String toString(){
        StringWriter stringWriter = new StringWriter();
        try{
            unparse(stringWriter);
        } catch(IOException x){

        }
        return stringWriter.toString();
    }

}
