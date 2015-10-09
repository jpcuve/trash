package graphic.gpp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class GCanvas extends JComponent implements MouseListener, MouseMotionListener {
    private List<GListener> listeners = new ArrayList<GListener>();
    private Vector ds;
    private Vector cs;
    private GShape selection;
    private Point mouseInit = null;
    private Point mouseLast = null;

    public GCanvas() {
        this.ds = new Vector();
        this.cs = new Vector();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void mouseClicked(MouseEvent e) {
        if(this.selection != null){
            for (GListener gl: listeners) gl.shapeClick(new GEvent(this.selection));
        }
    }

    public void mousePressed(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());
        this.mouseInit = p;
        this.mouseLast = null;
        GShape hit = null;
        for(Enumeration e1 = ds.elements(); e1.hasMoreElements() && hit == null;){
            GShape d = (GShape)e1.nextElement();
            if(d.pick(p)) hit = d;
        }
        if(hit != null){
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            this.selection = hit;
            for (GListener gl: listeners) gl.shapeMouseDown(new GEvent(this.selection));
        }else{
            this.selection = null;
        }
    }

    public void mouseReleased(MouseEvent e) {
        Graphics g = getGraphics();
        if(this.mouseLast != null){
            int dx = this.mouseLast.x - this.mouseInit.x;
            int dy = this.mouseLast.y - this.mouseInit.y;
            this.selection.outline(g, dx, dy);
            this.mouseLast = null;
            this.selection.invalidate(this);
            for(Enumeration e1 = cs.elements(); e1.hasMoreElements();){
                GConnector c = (GConnector)e1.nextElement();
                if(c.isConnected(this.selection)) c.invalidate(this);
            }
            this.selection.offset(dx, dy);
            this.selection.invalidate(this);
            for(Enumeration e1 = cs.elements(); e1.hasMoreElements();){
                GConnector c = (GConnector)e1.nextElement();
                if(c.isConnected(this.selection)) c.invalidate(this);
            }
            repaint();
            for (GListener gl: listeners) gl.shapeMouseUp(new GEvent(this.selection, dx, dy));
            this.setCursor(Cursor.getDefaultCursor());
        }
        this.mouseInit = null;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());
        if(this.mouseInit != null && this.selection != null){
            Graphics g = getGraphics();
            if(mouseLast != null){
                int dx = this.mouseLast.x - this.mouseInit.x;
                int dy = this.mouseLast.y - this.mouseInit.y;
                this.selection.outline(g, dx, dy);
            }
            int dx = p.x - this.mouseInit.x;
            int dy = p.y - this.mouseInit.y;
            this.selection.outline(g, dx, dy);
            mouseLast = p;
            for (GListener gl: listeners) gl.shapeMouseMove(new GEvent(this.selection, dx, dy));
        }
    }

    @Override
    public void paint(Graphics g) {
        for(Enumeration e1 = cs.elements(); e1.hasMoreElements();){
            GConnector c = (GConnector)e1.nextElement();
            c.draw(g);
        }
        for(Enumeration e1 = ds.elements(); e1.hasMoreElements();){
            GShape d = (GShape)e1.nextElement();
            d.draw(g);
        }
    }

    private void GCanvas_doubleClick(Object source, Event e){
        if(this.selection != null){
            for (GListener gl: listeners) gl.shapeDoubleClick(new GEvent(this.selection));
        }
    }


    public void addGListener(GListener l){
        listeners.add(l);
    }

    public void removeGListener(GListener l){
        listeners.remove(l);
    }

    // drawables

    public void addShape(GShape go){
        ds.addElement(go);
    }

    public void removeShape(GShape go){
        ds.removeElement(go);
    }

    public void clearShapes(){
        ds.removeAllElements();
    }

    public boolean containsShape(GShape go){
        return ds.contains(go);
    }

    public void addConnector(GConnector c){
        cs.addElement(c);
    }

    public void removeConnector(GConnector c){
        cs.removeElement(c);
    }

    public void clearConnectors(){
        cs.removeAllElements();
    }

    public boolean containsConnector(GConnector c){
        return cs.contains(c);
    }

    public void bringToFront(GShape go) throws ArrayIndexOutOfBoundsException{
        int index = ds.indexOf(go);
        if(index != -1){
            this.removeShape(go);
            ds.insertElementAt(go, 0);
        }
    }

    public void sendToBack(GShape go) throws ArrayIndexOutOfBoundsException {
        if(this.containsShape(go)){
            this.removeShape(go);
            this.addShape(go);
        }
    }

    public static void main(String[] args) {
        GCanvas canvas = new GCanvas();
        canvas.addShape(new GSM(10, 10));
        JFrame frame = new JFrame();
        frame.add(canvas);
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
