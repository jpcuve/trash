/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Sep 4, 2002
 * Time: 6:14:57 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.smalltalk;

public class Closure {
    private Segment body;

    public Closure(Segment body) {
        this.body = body;
    }

    public String toString() {
        return "( closure[\n" + body + " )";
    }
}
