package swing.autoscroll;

import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 25, 2006
 * Time: 5:25:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class CellEvent extends EventObject {
    private int x;
    private int y;
    private MouseEvent mouseEvent;

    public CellEvent(Object source, int x, int y, MouseEvent mouseEvent) {
        super(source);
        this.x = x;
        this.y = y;
        this.mouseEvent = mouseEvent;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public MouseEvent getMouseEvent() {
        return mouseEvent;
    }
}
