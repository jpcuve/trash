/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: 14-May-02
 * Time: 17:16:51
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.calculator;

import javax.swing.event.EventListenerList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

public class Machine {
    public static final int NOP = 0;
    public static final int BRA = 1;
    public static final int BEQ = 2;
    public static final int BNE = 3;
    public static final int LVALUE = 4;
    public static final int RVALUE = 5;
    public static final int DISP = 6;
    public static final int ASSIGN = 7;
    public static final int INTEGER = 8;
    public static final int FLOATINGPOINT = 9;
    public static final int LT = 10;
    public static final int LE = 11;
    public static final int EQ = 12;
    public static final int GE = 13;
    public static final int GT = 14;
    public static final int NE = 15;
    public static final int ADD = 16;
    public static final int SUB = 17;
    public static final int MUL = 18;
    public static final int DIV = 19;
    public static final int NEG = 20;

    private static final String[] OP = {"nop", "bra", "beq", "bne", "lvalue", "rvalue", "disp", "assign", "integer",
        "floatingpoint", "lt", "le", "eq", "ge", "gt", "ne", "add", "sub", "mul", "div", "neg"};

    private EventListenerList ell;
    private Instruction first;
    private Instruction current;
    private HashMap labels;
    private long labelCount;
    private Stack st;

    private class Instruction{
        private int op;
        private String arg;
        private Instruction next;

        public Instruction(int op, String arg){
            this.op = op;
            this.arg = arg;
            this.next = null;
        }

        public Instruction execute(Machine m, Environment e){
            String v1, v2;
            double d1, d2;
            Instruction ci;
            switch(op){
                case NOP:
                    break;
                case BRA:
                    ci = (Instruction)m.labels.get(arg);
                    return ci;
                case BEQ:
                    v1 = m.pop();
                    d1 = new Double(v1).doubleValue();
                    if (d1 == 0){
                        ci = (Instruction)m.labels.get(arg);
                        return ci;
                    }
                    break;
                case BNE:
                    v1 = m.pop();
                    d1 = new Double(v1).doubleValue();
                    if (d1 != 0){
                        ci = (Instruction)m.labels.get(arg);
                        return ci;
                    }
                    break;
                case DISP:
                    v1 = m.pop();
                    m.fireMachineDisplay(v1);
                    break;
                case ASSIGN:
                    v1 = m.pop();
                    v2 = m.pop();
                    e.bind(v2, v1);
                    break;
                case LT:
                    v2 = m.pop();
                    v1 = m.pop();
                    d2 = new Double(v2).doubleValue();
                    d1 = new Double(v1).doubleValue();
                    m.push(d1 < d2 ? "1" : "0");
                    break;
                case LE:
                    v2 = m.pop();
                    v1 = m.pop();
                    d2 = new Double(v2).doubleValue();
                    d1 = new Double(v1).doubleValue();
                    m.push(d1 <= d2 ? "1" : "0");
                    break;
                case EQ:
                    v2 = m.pop();
                    v1 = m.pop();
                    d2 = new Double(v2).doubleValue();
                    d1 = new Double(v1).doubleValue();
                    m.push(d1 == d2 ? "1" : "0");
                    break;
                case GE:
                    v2 = m.pop();
                    v1 = m.pop();
                    d2 = new Double(v2).doubleValue();
                    d1 = new Double(v1).doubleValue();
                    m.push(d1 >= d2 ? "1" : "0");
                    break;
                case GT:
                    v2 = m.pop();
                    v1 = m.pop();
                    d2 = new Double(v2).doubleValue();
                    d1 = new Double(v1).doubleValue();
                    m.push(d1 > d2 ? "1" : "0");
                    break;
                case NE:
                    v2 = m.pop();
                    v1 = m.pop();
                    d2 = new Double(v2).doubleValue();
                    d1 = new Double(v1).doubleValue();
                    m.push(d1 != d2 ? "1" : "0");
                    break;
                case ADD:
                    v2 = m.pop();
                    v1 = m.pop();
                    d2 = new Double(v2).doubleValue();
                    d1 = new Double(v1).doubleValue();
                    m.push(new Double(d1 + d2).toString());
                    break;
                case SUB:
                    v2 = m.pop();
                    v1 = m.pop();
                    d2 = new Double(v2).doubleValue();
                    d1 = new Double(v1).doubleValue();
                    m.push(new Double(d1 - d2).toString());
                    break;
                case MUL:
                    v2 = m.pop();
                    v1 = m.pop();
                    d2 = new Double(v2).doubleValue();
                    d1 = new Double(v1).doubleValue();
                    m.push(new Double(d1 * d2).toString());
                    break;
                case DIV:
                    v2 = m.pop();
                    v1 = m.pop();
                    d2 = new Double(v2).doubleValue();
                    d1 = new Double(v1).doubleValue();
                    m.push(new Double(d1 / d2).toString());
                    break;
                case LVALUE:
                    m.push(arg);
                    break;
                case RVALUE:
                    v1 = e.lookup(arg);
                    m.push(v1);
                    break;
                case INTEGER:
                    m.push(arg);
                    break;
                case FLOATINGPOINT:
                    m.push(arg);
                    break;
                case NEG:
                    v1 = m.pop();
                    d1 = new Double(v1).doubleValue();
                    m.push(new Double(-d1).toString());
                    break;
            }
            return next;
        }

        public String toString(){
            return (op == NOP) ? "" + arg + ":" : "" + OP[op] + ((arg == null) ? "" : "(" + arg + ")");
        }

    }

    public Machine() {
        ell = new EventListenerList();
        labels = new HashMap();
        labelCount = 0;
        st = new Stack();
        clear();
    }

    public void addMachineListener(MachineListener ml) {
        ell.add(MachineListener.class, ml);
    }

    public void removeMachineListener(MachineListener ml) {
        ell.remove(MachineListener.class, ml);
    }

    protected void fireMachineDisplay(String s) {
        Object[] ls = ell.getListenerList();
        for(int i = ls.length - 2; i >= 0; i-= 2) {
            ((MachineListener)ls[i + 1]).machineDisplay(new MachineEvent(this, s));
        }
    }

    public String nextLabel(){
        return "L" + labelCount++;
    }

    public void clear() {
        first = null;
        current = null;
    }

    public void addLabel(String s){
        Instruction ci = new Instruction(NOP, s);
        labels.put(s, ci);
        add(ci);
    }

    public void addImplicit(int co){
        Instruction ci = new Instruction(co, null);
        add(ci);
    }

    public void addImmediate(int co, String s){
        Instruction ci = new Instruction(co, s);
        add(ci);
    }

    private void add(Instruction ci){
        if (first == null){
            first = ci;
            current = ci;
        } else{
            current.next = ci;
            current = ci;
        }
    }

    public void run(Environment e){
        Instruction ci = first;
        while(ci != null){
            System.out.print("Exec: " + ci + " - Stack: ");
            ci = ci.execute(this, e);
            for(Iterator i = st.iterator(); i.hasNext();) {
                System.out.print("'" + i.next() + "' ");
            }
            System.out.println();
        }
    }


    // basic Forth implementation


    public void push(String s){
        st.push(s);
    }

    public String pop(){
        return (String)st.pop();
    }

    public void dup(){
        st.push(st.peek());
    }

    public void over(){
        Object o1 = st.pop();
        Object o2 = st.peek();
        st.push(o1);
        st.push(o2);
    }

    public void drop(){
        st.pop();
    }

    public void swap(){
        Object o1 = st.pop();
        Object o2 = st.pop();
        st.push(o1);
        st.push(o2);
    }
}
