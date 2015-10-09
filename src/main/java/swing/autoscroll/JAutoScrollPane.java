package swing.autoscroll;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 25, 2006
 * Time: 2:36:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class JAutoScrollPane extends JScrollPane {
    public JAutoScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        construct();
    }

    public JAutoScrollPane(Component view) {
        super(view);
        construct();
    }

    public JAutoScrollPane(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
        construct();
    }

    public JAutoScrollPane() {
        construct();
    }

    private void construct(){
        final Component view = getViewport().getView();
        if (view != null) {
            view.addMouseListener(new MouseListener(){
                public void mouseClicked(MouseEvent e) {
                }

                public void mousePressed(MouseEvent e) {
                }

                public void mouseReleased(MouseEvent e) {
                    view.setCursor(Cursor.getDefaultCursor());
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }
            });
            view.addMouseMotionListener(new MouseMotionListener(){
                public void mouseDragged(MouseEvent e) {
                    view.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
//                    System.out.printf("%s: mouse dragged: %s%n", getClass().getName(), licenseType);
                    BoundedRangeModel horzModel = getHorizontalScrollBar().getModel();
                    BoundedRangeModel vertModel = getVerticalScrollBar().getModel();
//                    System.out.printf("horizontal scroll bar, min: %s, max: %s, val: %s%n", horzModel.getMinimum(), horzModel.getMaximum(), horzModel.getValue());
                    Rectangle bounds = getViewport().getBounds();
//                    System.out.printf("viewport dimension: %s%n", bounds);
                    Point p = getViewport().getViewPosition();
//                    System.out.printf("view position: %s%n", p);
                    int x = e.getX() - p.x; // x position on viewport
                    int y = e.getY() - p.y;
//                    System.out.printf("x, y: %s, %s%n", x, y);
                    horzModel.setValue(view.getWidth() * x / (getViewport().getWidth()));
                    vertModel.setValue(view.getHeight() * y / (getViewport().getHeight()));
                }

                public void mouseMoved(MouseEvent e) {
                }
            });
        }
    }



    public static void main(String[] args) {
        TableModel tableModel = new AbstractTableModel(){
            public int getRowCount() {
                return 10;
            }

            public int getColumnCount() {
                return 10;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                return rowIndex * columnIndex;
            }
        };
        JFrame frame = new JFrame();
        JGrid grid = new JGrid(tableModel);
        JAutoScrollPane pane = new JAutoScrollPane(grid);
        pane.setOpaque(true);
        JHeader horzHeader = new JHeader(grid.getColumnModel(), JHeader.Orientation.HORIZONTAL, 15);
        pane.setColumnHeaderView(horzHeader);
        JHeader vertHeader = new JHeader(grid.getRowModel(), JHeader.Orientation.VERTICAL, 60);
        pane.setRowHeaderView(vertHeader);
        frame.add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

}
