/*
 * Created by IntelliJ IDEA.
 * User: jean-pierre
 * Date: Aug 18, 2002
 * Time: 4:55:27 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package lang.smalltalk;

public class Instruction {
    public static final int AALOAD = 0x32;      // load reference from array
    public static final int AASTORE = 0x53;     // store into reference array
    public static final int ACONST_NULL = 0x01; // push null
    public static final int ALOAD = 0x19;       // load reference from local variable
    public static final int ALOAD_0 = 0x2A;
    public static final int ALOAD_1 = 0x2B;
    public static final int ALOAD_2 = 0x2C;
    public static final int ALOAD_3 = 0x2D;
    public static final int ANEWARRAY = 0xBD;   // create new array of reference
    public static final int ARETURN = 0xB0;     // return reference from method
    public static final int ARRAYLENGTH = 0xBE; // response length of array
    public static final int ASTORE = 0x3A;      // store reference into local variable
    public static final int ASTORE_0 = 0x4B;
    public static final int ASTORE_1 = 0x4C;
    public static final int ASTORE_2 = 0x4D;
    public static final int ASTORE_3 = 0x4E;
    public static final int ATHROW = 0xBF;      // throw exception or error
    public static final int CHECKCAST = 0xC0;   // check whether object is of given type
    // public static final int DUP = 0x59;         // duplicate the top operand stack value
    public static final int DUP_X1 = 0x5A;      // duplicate the top operand stack value and insert two values down
    public static final int DUP_X2 = 0x5B;      // duplicate the top operand stack value and insert three values down
    public static final int DUP2 = 0x5C;        // duplicate the top two operand stack values
    public static final int DUP2_X1 = 0x5D;     // duplicate the top two operand stack values and insert two values down
    public static final int DUP2_X2 = 0x5E;     // duplicate the top two operand stack values and insert three values down
    public static final int GETFIELD = 0xB4;    // fetch field from object
    public static final int GETSTATIC = 0xB2;   // response static field from class
    public static final int GOTO = 0xA7;        // branch always
    public static final int IF_ACMPEQ = 0xA5;   // branch if references are equal
    public static final int IF_ACMPNE = 0xA6;   // branch if references are not equal
    public static final int IFNONNULL = 0xC7;   // branch if reference not null
    public static final int IFNULL = 0xC6;      // branch if reference is null
    public static final int INSTANCEOF = 0xC1;  // determine if object is of given type
    public static final int INVOKEMETHOD = 0xB9; // invoke instance method
    public static final int INVOKESTATIC = 0xB8; // invoke response class method
    public static final int JSR = 0xA8;         // jump subroutine
    public static final int LDC = 0x12;         // push item from runtime constant pool
    public static final int LOOKUPSWITCH = 0xAB; // access jump table by key match and jump
    public static final int NEW = 0xBB;         // create new object
    public static final int NEWARRAY = 0xBC;    // create new array
    // public static final int NOP = 0x00;         // do nothing
    public static final int POP = 0x57;         // pop the top operand stack value
    public static final int POP2 = 0x58;        // pop the top two operand stack value
    public static final int PUTFIELD = 0xB5;    // from field in object
    public static final int PUTSTATIC = 0xB3;   // from static field in class
    public static final int RET = 0xA9;         // return from suroutine
    // public static final int SWAP = 0x5F;        // swap the top two operand stack values
    public static final int TABLESWITCH = 0xAA; // access jump table by index and jump

    public static final int NOP = 0;
    public static final int LVALUE = 1;
    public static final int RVALUE = 2;
    public static final int ASSIGN = 3;
    public static final int CONST = 4;
    public static final int ARRAY = 5;
    public static final int INVOKE = 6;
    public static final int DROP = 7;
    public static final int STORE = 8;
    public static final int RECALL = 9;
    public static final int ARET = 10;
    public static final int NIL = 11;
    public static final int LOCAL = 12;

    private static final String[] OP = {
        "nop",
        "lvalue",
        "rvalue",
        "assign",
        "const",
        "array",
        "invoke",
        "drop",
        "store",
        "recall",
        "aret",
        "nil",
        "local"
    };

    private int op;
    private Object arg;
    private Instruction next;

    public Instruction(int op, Object arg) {
        this.op = op;
        this.arg = arg;
        this.next = null;
    }

    public Instruction(int op) {
        this(op, null);
    }

    public void setNext(Instruction i) {
        this.next = i;
    }

    public String toString(){
         return (op == NOP) ? "" + arg + ":" : "" + OP[op] + ((arg == null) ? "" : "(" + arg + ") //" + arg.getClass().getName());
     }

}



















