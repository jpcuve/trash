package graphic.gpp;

import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;

public class GPicture extends GShape{

    private Vector ds;

    public GPicture(int x, int y){
        super(x, y, 0, 0);
        ds = new Vector();
        this.setTextPosition(TextPosition.BOTTOM);
    }

    // overload

    public void drawShape(Graphics g, int dx, int dy){
        Point pos = this.getPosition();
        for(Enumeration e1 = ds.elements(); e1.hasMoreElements();){
            GShape d = (GShape)e1.nextElement();
            d.drawShape(g, pos.x + dx, pos.y + dy);
        }
    }

    public void outlineShape(Graphics g, int dx, int dy){
        Point pos = this.getPosition();
        for(Enumeration e1 = ds.elements(); e1.hasMoreElements();){
            GShape d = (GShape)e1.nextElement();
            d.outlineShape(g, pos.x + dx, pos.y + dy);
        }
    }

    public boolean pick(Point p, int dx, int dy){
        Point pos = this.getPosition();
        for(Enumeration e1 = ds.elements(); e1.hasMoreElements();){
            GShape d = (GShape)e1.nextElement();
            if(d.pick(p, pos.x + dx, pos.y + dy)) return true;;
        }
        return false;
    }

    public Point getExtent(){
        Rectangle r = null;
        for(Enumeration e1 = ds.elements(); e1.hasMoreElements();){
            GShape d = (GShape)e1.nextElement();
            Rectangle rn = d.getOutline();
            r = (r == null) ? rn : r.union(rn);
        }
        return new Point(r.width, r.height);
    }

    // miscellaneous

    public void add(GShape d){
        ds.addElement(d);
    }

    public void remove(GShape d){
        ds.removeElement(d);
    }

    public boolean contains(GShape d){
        return ds.contains(d);
    }

}
