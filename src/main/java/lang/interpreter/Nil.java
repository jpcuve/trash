/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jul 26, 2002
 * Time: 5:04:40 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.interpreter;

public class Nil extends Expression implements List {
    public static final Nil NIL = new Nil();

    public Expression append(Expression z){
		return z;
	}

    public long length() {
        return 0;
    }

    public boolean isList() {
        return true;
    }

    public String write() {
        return "()";
    }
}
