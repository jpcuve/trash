package graphic.gpp;

import java.awt.*;

public class GLine extends GShape {
    private Point p1;
    private Point p2;

    public GLine(Point p1, Point p2){
        super(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
        this.p1 = new Point(p1.x, p1.y);
        this.p2 = new Point(p2.x, p2.y);
    }


    // overload

    public Point getPosition(){
        return new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
    }

    public Point getExtent(){
        return new Point(Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
    }

    public Rectangle getOutline(){
        Point pos = this.getPosition();
        Point wh = this.getExtent();
        return new Rectangle(pos.x, pos.y, wh.x + 1, wh.y + 1);
    }

    public void drawShape(Graphics g, int dx, int dy){
        g.drawLine(p1.x + dx, p1.y + dy, p2.x + dx, p2.y + dy);
    }

    public void outlineShape(Graphics g, int dx, int dy){
        g.setXORMode(Color.WHITE);
        g.drawLine(p1.x + dx, p1.y + dy, p2.x + dx, p2.y + dy);
    }

    public boolean pick(Point p, int dx, int dy){
        int lx = p.x - dx - p1.x;
        int ly = p.y - dy - p1.y;
        int totx = p2.x - p1.x;
        int toty = p2.y - p1.y;
        if(ly * totx != lx * toty) return false;
        if(Math.abs(lx) > Math.abs(totx)) return false;
        if(Math.abs(ly) > Math.abs(toty)) return false;
        return true;
    }

    public void offset(int dx, int dy){
        this.p1.x += dx;
        this.p1.y += dy;
        this.p2.x += dx;
        this.p2.y += dy;
    }

}
