package graphic.gpp;

import java.awt.*;

public class GRectangle extends GShape{

    public GRectangle(int x, int y, int w, int h){
        super(x, y, w, h);
    }

    public GRectangle(Rectangle r){
        super(r.x, r.y, r.width, r.height);
    }

    public void drawShape(Graphics g, int dx, int dy){
        Point pos = this.getPosition();
        Point wh = this.getExtent();
        g.drawRect(pos.x + dx, pos.y + dy, wh.x, wh.y);
    }

    public void outlineShape(Graphics g, int dx, int dy){
        Point pos = this.getPosition();
        Point wh = this.getExtent();
        g.setXORMode(Color.WHITE);
        g.drawRect(pos.x + dx, pos.y + dy, wh.x, wh.y);
    }

    public boolean pick(Point p, int dx, int dy){
        Rectangle r = this.getOutline();
        r.x += dx;
        r.y += dy;
        return r.contains(p);
    }

}
