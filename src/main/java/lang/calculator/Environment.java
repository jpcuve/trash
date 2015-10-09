/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: 14-May-02
 * Time: 17:28:22
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.calculator;

import java.util.HashMap;

public class Environment {
    private HashMap bindings;

    public Environment(){
        bindings = new HashMap();
    }

    public void bind(String k, String v){
        if (bindings.keySet().contains(k)){
            bindings.put(k, v);
        } else{
            bindings.put(k, v);
        }
    }

    public String lookup(String k){
        return (String)bindings.get(k);
    }
}
