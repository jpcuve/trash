/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Sep 3, 2002
 * Time: 7:09:36 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.smalltalk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class Segment {
    private Instruction first;
    private Instruction current;
    private int labelCount;
    private HashMap labels;
    private Vector instructions;

    public Segment() {
        first = current = null;
        labelCount = 0;
        labels = new HashMap();
        instructions = new Vector();
    }

    public String addLabel(){
        String label = "L" +labelCount++;
        Instruction ci = new Instruction(Instruction.NOP, label);
        labels.put(label, ci);
        add(ci);
        return label;
    }

    public void addImplicit(int co){
        Instruction ci = new Instruction(co, null);
        add(ci);
    }

    public void addImmediate(int co, Object o){
        Instruction ci = new Instruction(co, o);
        add(ci);
    }

    private void add(Instruction ci){
        if (first == null){
            first = ci;
            current = ci;
        } else{
            current.setNext(ci);
            current = ci;
        }
        instructions.add(ci);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Iterator i = instructions.iterator(); i.hasNext();) {
            sb.append(i.next());
            sb.append("\n");
        }
        return sb.toString();
    }
}
