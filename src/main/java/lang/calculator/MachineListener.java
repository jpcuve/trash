/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: 15-May-02
 * Time: 15:02:53
 * To change template for new interface use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.calculator;

import java.util.EventListener;

public interface MachineListener extends EventListener {
    public void machineDisplay(MachineEvent ev);
}
