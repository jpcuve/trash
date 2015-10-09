/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Sep 6, 2002
 * Time: 4:04:48 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.smalltalk;

public class Method {
    private Segment body;

    public Method(Segment body) {
        this.body = body;
    }

    public String toString() {
        return "( method[\n" + body + " )";
    }
}
