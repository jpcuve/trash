package lang.assembler;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 12, 2004
 * Time: 10:09:54 AM
 * To change this template use File | Settings | File Templates.
 */
public enum Mode {
    IMP(1), ACC(1), IMM1(2), IMM2(2), ZP(2), ZPX(2), ZPY(2), ZPXII(2), ZPIYI(2), ABS(3), ABSX(3), ABSY(3), ABSY2(3), ABSI(3), REL(2);

    private final int size;

    Mode(int size){
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
