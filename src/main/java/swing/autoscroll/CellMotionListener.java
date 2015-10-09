package swing.autoscroll;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 25, 2006
 * Time: 5:24:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CellMotionListener {
    void cellDragged(CellEvent e);
    void cellMoved(CellEvent e);
}
