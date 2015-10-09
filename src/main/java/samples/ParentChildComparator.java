/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: 23-May-02
 * Time: 16:35:57
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package samples;

import java.util.Comparator;

public class ParentChildComparator implements Comparator {
    public int compare(Object o, Object o1) {
        ParentChild pc = (ParentChild) o;
        ParentChild pc1 = (ParentChild) o1;
        if (pc.getParent() == pc1) return -1;
        if (pc1.getParent() == pc) return 1;
        return 0;
    }
}
