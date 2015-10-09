package swing.autoscroll;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 25, 2006
 * Time: 8:13:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class JGrid extends JPanel implements ChangeListener, TableModelListener {
    private TableModel model = new DefaultTableModel();
    private HeaderModel columnModel = new HeaderModel(10, 100);
    private HeaderModel rowModel = new HeaderModel(20, 15);
    private TableCellRenderer cellRenderer = new DefaultTableCellRenderer();
    private Color gridColor = Color.BLACK;

    private JTable table = new JTable();

    public JGrid() {
        init();
    }

    public JGrid(TableModel model) {
        this.model = model;
        init();
    }

    private void init(){
        model.addTableModelListener(this);
        columnModel.addChangeListener(this);
        rowModel.addChangeListener(this);
    }

    public TableModel getModel() {
        return model;
    }

    public HeaderModel getColumnModel() {
        return columnModel;
    }

    public void setColumnModel(HeaderModel columnModel) {
        this.columnModel = columnModel;
    }

    public HeaderModel getRowModel() {
        return rowModel;
    }

    public void setRowModel(HeaderModel rowModel) {
        this.rowModel = rowModel;
    }

    public TableCellRenderer getCellRenderer() {
        return cellRenderer;
    }

    public void setCellRenderer(TableCellRenderer cellRenderer) {
        this.cellRenderer = cellRenderer;
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    public void stateChanged(ChangeEvent e) {
        setSize(getPreferredSize());
    }

    public void tableChanged(TableModelEvent e) {
        repaint();
    }

    public void paint(Graphics g) {
        Graphics2D d = (Graphics2D)g;
        int w = getWidth();
        int h = getHeight();
        d.setColor(getBackground());
        d.fillRect(0, 0, w, h);
        Dimension size = getPreferredSize();
        Component render = getCellRenderer().getTableCellRendererComponent(table, "test", false, false, 0, 0);
        d.setColor(render.getBackground());
        d.fillRect(0, 0, size.width, size.height);
        d.setColor(getGridColor());
        int x = 0, y = 0;
        for (int ch: columnModel.getExtents()){
            x += ch;
            d.drawLine(x, 0, x, size.height);
        }
        for (int cv: rowModel.getExtents()){
            y += cv;
            d.drawLine(0, y, size.width, y);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(columnModel.getTotalExtent(), rowModel.getTotalExtent());
    }
}
