package swing.autoscroll;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 25, 2006
 * Time: 5:24:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CellListener {
    void cellClicked(CellEvent event);
    void cellEntered(CellEvent event);
    void cellExited(CellEvent event);
    void cellPressed(CellEvent event);
    void cellReleased(CellEvent event);
}
