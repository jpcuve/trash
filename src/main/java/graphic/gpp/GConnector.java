package graphic.gpp;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class GConnector extends GObject{

    private static int MAX_ABS_DEVIATION = 4;
    private static int ARROW_SIZE = 10;
    public static int NONE = 0;
    public static int FIRST_TO_SECOND = 1;
    public static int SECOND_TO_FIRST = 2;
    public static int CIRCLE = 3;

    private GObject s1;
    private GObject s2;
    private int typ;
    private int dev;

    public GConnector(GObject s1, GObject s2, int typ, int dev){
        this.setBegin(s1);
        this.setEnd(s2);
        this.setType(typ);
        this.setDeviation(dev);
    }

    public void setBegin(GObject s1){
        this.s1 = s1;
    }

    public GObject getBegin(){
        return this.s1;
    }

    public void setEnd(GObject s2){
        this.s2 = s2;
    }

    public GObject getEnd(){
        return this.s2;
    }

    public void setType(int typ){
        this.typ = typ;
    }

    public int getType(){
        return this.typ;
    }

    public void setDeviation(int dev){
        this.dev = Math.max(-GConnector.MAX_ABS_DEVIATION, Math.min(GConnector.MAX_ABS_DEVIATION, dev));
    }

    public int getDeviation(){
        return this.dev;
    }

    public boolean isConnected(GObject s){
        return (s == s1 || s == s2);
    }

    private Point getBeginDrill(){
        Point c1 = s1.getCenter();
        Point c2 = s2.getCenter();
        int dx = c2.x - c1.x;
        int dy = c2.y - c1.y;
        return s1.getDrill(dx, dy);
    }

    private Point getEndDrill(){
        Point c1 = s1.getCenter();
        Point c2 = s2.getCenter();
        int dx = c2.x - c1.x;
        int dy = c2.y - c1.y;
        return s2.getDrill(-dx, -dy);
    }

    public void drawShape(Graphics g, int offx, int offy){
        Rectangle r1 = s1.getOutline();
        Rectangle r2 = s2.getOutline();
        if(!r1.intersects(r2)){
            Point p1 = this.getBeginDrill();
            Point p2 = this.getEndDrill();
            int perpdx = (p2.y - p1.y);
            int perpdy = (p1.x - p2.x);
            int dx = (p2.x - p1.x);
            int dy = (p2.y - p1.y);
            int s = (int)Math.sqrt((double)(perpdx * perpdx) + (double)(perpdy * perpdy));
            if(this.getDeviation() == 0){
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }else{
                Point pi = this.getDeviMid();
                Point[] pb = new Point[4];
                pb[0] = p1;
                /*
                    p[1] = new Point((p1.x + pi.x) / 2, (p1.y + pi.y) / 2);
                    p[2] = new Point((p2.x + pi.x) / 2, (p2.y + pi.y) / 2);
                    */
                pb[1] = pi;
                pb[2] = pi;
                pb[3] = p2;
                // minorVersion.drawPolygon(p, RasterOp.TARGET.invert());
// TODO               minorVersion.drawBezier(pb);
            }
            Point pc = this.getCenter();
            int sdx = (dx * GConnector.ARROW_SIZE) / s / 2;
            int sdy = (dy * GConnector.ARROW_SIZE) / s / 2;
            int sperpdx = (perpdx * GConnector.ARROW_SIZE) / (2 * s);
            int sperpdy = (perpdy * GConnector.ARROW_SIZE) / (2 * s);
            int[] pax = new int[3];
            int[] pay = new int[3];
            if(this.getType() == GConnector.FIRST_TO_SECOND){
                pax[0] = pc.x - sdx + sperpdx; pay[0] = pc.y - sdy + sperpdy;
                pax[1] = pc.x + sdx; pay[1] = pc.y + sdy;
                pax[2] = pc.x - sdx - sperpdx; pay[2] = pc.y - sdy - sperpdy;
                g.drawPolygon(pax, pay, 3);
            }else if(this.getType() == GConnector.SECOND_TO_FIRST){
                pax[0] = pc.x + sdx + sperpdx; pay[0] = pc.y + sdy + sperpdy;
                pax[1] = pc.x - sdx; pay[1] = pc.y - sdy;
                pax[2] = pc.x + sdx - sperpdx; pay[1] = pc.y + sdy - sperpdy;
                g.drawPolygon(pax, pay, 3);
            }else if(this.getType() == GConnector.CIRCLE){
                Rectangle r = new Rectangle(pc.x + 1 - GConnector.ARROW_SIZE / 2, pc.y  + 1 - GConnector.ARROW_SIZE / 2, GConnector.ARROW_SIZE, GConnector.ARROW_SIZE);
                g.drawOval(r.x, r.y, r.width, r.height);
            }
        }
    }

    public void drawText(Graphics g){
        Rectangle r1 = s1.getOutline();
        Rectangle r2 = s2.getOutline();
        if(!r1.intersects(r2)){
            g.setFont(this.getTextFont());
            String s = this.getText();
            FontMetrics fontMetrics = g.getFontMetrics(getTextFont());
            Rectangle2D d = fontMetrics.getStringBounds(s, g);
            Point p = new Point((int)d.getWidth(), (int)d.getHeight());
            this.setTextExtent(p);
            Point pc = this.getCenter();
            g.setColor(this.getTextColor());
            g.drawString(s, pc.x - p.x / 2, pc.y + 1 + GConnector.ARROW_SIZE / 2);
        }
    }

    public Rectangle getOutline(){
        Point p1 = this.getBeginDrill();
        Point p2 = this.getEndDrill();
        Point pi = this.getDeviMid();
        Point pc = this.getCenter();
        Rectangle r1 = new Rectangle(Math.min(p1.x, pi.x), Math.min(p1.y, pi.y), Math.abs(p1.x - pi.x), Math.abs(p1.y - pi.y));
        Rectangle r2 = new Rectangle(Math.min(p2.x, pi.x), Math.min(p2.y, pi.y), Math.abs(p2.x - pi.x), Math.abs(p2.y - pi.y));
        Rectangle r = r1.union(r2);
        if(this.getType() != GConnector.NONE) r = r.union(new Rectangle(pc.x - GConnector.ARROW_SIZE / 2, pc.y - GConnector.ARROW_SIZE / 2, GConnector.ARROW_SIZE, GConnector.ARROW_SIZE));
        return r;
    }


    public Point getPosition(){
        Rectangle r = this.getOutline();
        return new Point(r.x, r.y);
    }

    public Point getExtent(){
        Rectangle r = this.getOutline();
        return new Point(r.width, r.width);
    }

    public Point getCenter(){
        Point p1 = this.getBeginDrill();
        Point p2 = this.getEndDrill();
        Point pi = this.getDeviMid();
        int x1 = (p1.x + 3 * pi.x) / 4;
        int y1 = (p1.y + 3 * pi.y) / 4;
        int x2 = (p2.x + 3 * pi.x) / 4;
        int y2 = (p2.y + 3 * pi.y) / 4;
        return new Point((x1 + x2) / 2, (y1 + y2) / 2);
    }

    public void invalidate(Component ctl){
        Rectangle r = this.getOutline();
        Point pc = this.getCenter();
        Point p = this.getTextExtent();
        if(p != null){
            Rectangle rt = new Rectangle(pc.x - p.x / 2, pc.y + 1 + GConnector.ARROW_SIZE / 2, p.x, p.y);
            r = r.union(rt);
        }
        r.width++;
        r.height++;
        ctl.repaint(r.x, r.y, r.width, r.height);
    }

    private Point getDeviMid(){
        Point p1 = this.getBeginDrill();
        Point p2 = this.getEndDrill();
        int d = 2 * GConnector.MAX_ABS_DEVIATION;
        int perpdx = (p2.y - p1.y) / d;
        int perpdy = (p1.x - p2.x) / d;
        int dev = this.getDeviation();
        return new Point((p1.x + p2.x) / 2 + dev * perpdx, (p1.y + p2.y) / 2 + dev * perpdy);
    }

}
