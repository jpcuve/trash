package swing.autoscroll;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 25, 2006
 * Time: 4:21:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class JHeader extends JComponent implements ChangeListener, MouseMotionListener {
    private static final int GRAB_EXTENT = 3;
    public enum Orientation {
        VERTICAL, HORIZONTAL
    }
    protected HeaderModel model;
    protected Orientation orientation;
    private int span;
    private TableCellRenderer headerRenderer = new DefaultTableCellRenderer();
    private JTable table = new JTable();

    public JHeader() {
        this(Orientation.VERTICAL);
    }

    public JHeader(Orientation orientation) {
        this.orientation = orientation;
        int h = getFontMetrics(getFont()).getHeight();
        this.span = h + 2;
        this.model = new HeaderModel(10, h * 2);
        init();
    }

    public JHeader(Orientation orientation, int span, int count, int extent) {
        this.orientation = orientation;
        this.span = span;
        this.model = new HeaderModel(count, extent);
        init();
    }

    public JHeader(HeaderModel model, Orientation orientation, int span) {
        this.model = model;
        this.orientation = orientation;
        this.span = span;
        init();
    }

    private void init(){
        model.addChangeListener(this);
        this.addMouseMotionListener(this);
    }

    public HeaderModel getModel() {
        return model;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getSpan() {
        return span;
    }

    public TableCellRenderer getHeaderRenderer() {
        return headerRenderer;
    }

    public void setHeaderRenderer(TableCellRenderer headerRenderer) {
        this.headerRenderer = headerRenderer;
    }

    public void stateChanged(ChangeEvent e) {
        repaint();
    }

    private int resizing = 0;

    public void mouseDragged(MouseEvent e) {
        if (resizing > 0){
            int ref = model.getTotalExtent(resizing - 1);
            int extent = orientation == Orientation.VERTICAL ? e.getY() - ref : e.getX() - ref;
            if (extent < GRAB_EXTENT) extent = GRAB_EXTENT;
            model.setExtent(resizing - 1, extent);
        }
    }

    public void mouseMoved(MouseEvent e) {
        resizing = 0;
        if (orientation == Orientation.VERTICAL){
            int vv = 0;
            while (vv <= e.getY() && resizing < model.getExtents().size()) vv += model.getExtents().get(resizing++);
            if (Math.abs(vv - e.getY()) < GRAB_EXTENT){
                setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
            } else{
                resizing = 0;
                setCursor(Cursor.getDefaultCursor());
            }
        } else{
            int hh = 0;
            while (hh <= e.getX() && resizing < model.getExtents().size()) hh += model.getExtents().get(resizing++);
            if (Math.abs(hh - e.getX()) < GRAB_EXTENT){
                setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
            } else{
                resizing = 0;
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }


    public void paint(Graphics g) {
        Graphics2D d = (Graphics2D)g;
        int w = getWidth();
        int h = getHeight();
        int t = model.getTotalExtent();
        if (orientation == Orientation.VERTICAL){
            d.setColor(getBackground());
            d.fillRect(0, 0, w, t);
            d.setColor(getForeground());
            d.drawRect(0, 0, w - 1, t);
            int vv = 0;
            for (int i = 0; i < model.getExtents().size(); i++){
                int ch = model.getExtents().get(i);
                int lastv = vv;
                vv += ch;
                Component c = headerRenderer.getTableCellRendererComponent(table, i, false, false, 0, 0);
                SwingUtilities.paintComponent(g, c, this, 1, lastv + 1, w - 2, vv - lastv);
                d.drawLine(0, vv, w, vv);
            }
        } else{
            d.setColor(getBackground());
            d.fillRect(0, 0, t, h);
            d.setColor(getForeground());
            d.drawRect(0, 0, t, h - 1);
            int hh = 0;
            for (int i = 0; i < model.getExtents().size(); i++){
                int ch = model.getExtents().get(i);
                int lasth = hh;
                hh += ch;
                Component c = headerRenderer.getTableCellRendererComponent(table, i, false, false, 0, 0);
                SwingUtilities.paintComponent(g, c, this, lasth + 1, 1, hh - lasth, h - 2);
                d.drawLine(hh, 0, hh, h);
            }
        }
    }

    public Dimension getPreferredSize() {
        if (orientation == Orientation.VERTICAL){
            return new Dimension(span, model.getTotalExtent());
        } else{
            return new Dimension(model.getTotalExtent(), span);
        }
    }


}
