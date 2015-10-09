/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Sep 3, 2002
 * Time: 5:36:29 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.smalltalk;

import java.util.HashMap;
import java.util.Vector;

public class ClassInfo {
    private String name;
    private ClassInfo parent;
    private Vector fields;
    private HashMap methods;
}
