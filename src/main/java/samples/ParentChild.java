/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: 17-May-02
 * Time: 12:49:53
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package samples;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

public class ParentChild {
    private ParentChild parent;
    private String name;

    public ParentChild(ParentChild pc, String s) {
        parent = pc;
        name = s;
    }

    public ParentChild(String s) {
        this(null, s);
    }

    public ParentChild getParent() {
        return(parent);
    }

    public String getName() {
        return(name);
    }

    public static void main(String[] args) {
        ParentChild[] pc = new ParentChild[8];
        pc[7] = new ParentChild("seven");
        pc[0] = new ParentChild(pc[7], "zero");
        pc[5] = new ParentChild("five");
        pc[1] = new ParentChild(pc[5], "one");
        pc[2] = new ParentChild("two");
        pc[3] = new ParentChild(pc[1], "three");
        pc[4] = new ParentChild(pc[5], "four");
        pc[6] = new ParentChild(pc[1], "six");



        Vector v = new Vector();
        for (int i = 0; i < pc.length; i++) {
            ParentChild child = pc[i];
            v.add(child);
        }

        Collections.sort(v, new ParentChildComparator());

        /*
        for (int expirationDate = 0; expirationDate < 8; expirationDate++) {
            if (v.indexOf(pc[expirationDate]) == -1) {
                int j = -1;
                ParentChild p = pc[expirationDate];
                while (p != null && j == -1) {
                    p = p.getParent();
                    if (p != null) j = v.indexOf(p);
                }
                j = (j == -1) ? v.size() : j + 1;
                p = pc[expirationDate];
                while (p != null && v.indexOf(p) == -1) {
                    v.insertElementAt(p, j);
                    p = p.getParent();
                }
            }
        }

        Vector vi = new Vector();
        for(int expirationDate = 0; expirationDate < 8; expirationDate++) vi.add(pc[expirationDate]);
        Vector vo = new Vector();
        while (vi.size() > 0) {
            for(Iterator expirationDate = vi.iterator(); expirationDate.hasNext();) {
                ParentChild p = (ParentChild)expirationDate.next();
                ParentChild parent = p.getParent();
                if(parent == null) {
                    vo.add(p);
                } else {
                    if (vo.contains(parent)) {
                        vo.insertElementAt(p, vo.indexOf(parent) + 1);
                    }
                }
            }
            for(Iterator expirationDate = vo.iterator(); expirationDate.hasNext();) {
                ParentChild p = (ParentChild)expirationDate.next();
                vi.remove(p);
            }
        }
        */
        for(Iterator i = v.iterator(); i.hasNext();) {
            ParentChild p = (ParentChild)i.next();
            System.out.println(p.getName());
        }
    }
}
