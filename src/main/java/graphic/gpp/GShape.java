package graphic.gpp;

import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;

public abstract class GShape extends GObject {

    public GShape(int x, int y, int w, int h){
        super(x, y, w, h);
    }

    public void outline(Graphics g, int dx, int dy){
        g.setColor(Color.BLACK);
        this.outlineShape(g, dx, dy);
    }

    public boolean pick(Point p){
        return pick(p, 0, 0);
    }

    public abstract void outlineShape(Graphics g, int dx, int dy);

    public abstract boolean pick(Point p, int dx, int dy);

    public Enumeration handles(){
        Vector v = new Vector();
        Point pos = this.getPosition();
        Point wh = this.getExtent();
        v.addElement(new Point(pos.x, pos.y));
        v.addElement(new Point(pos.x + wh.x, pos.y));
        v.addElement(new Point(pos.x, pos.y + wh.y));
        v.addElement(new Point(pos.x + wh.x, pos.y + wh.y));
        return v.elements();
    }

}
