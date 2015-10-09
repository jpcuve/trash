/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: 15-May-02
 * Time: 15:04:08
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.calculator;

import java.util.EventObject;

public class MachineEvent extends EventObject {
    private String mess;

    public MachineEvent(Object source, String s){
        super(source);
        mess = s;
    }

    public String getMessage() {
        return mess;
    }
}
