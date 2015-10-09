package graphic.gpp;

import java.awt.*;

public class GEllipse extends GShape{

    public GEllipse(int x, int y, int w, int h){
        super(x, y, h, w);
    }

    // overload

    public void drawShape(Graphics g, int dx, int dy){
        Point pos = this.getPosition();
        Point wh = this.getExtent();
        g.drawOval(pos.x + dx, pos.y + dy, wh.x, wh.y);
    }

    public void outlineShape(Graphics g, int dx, int dy){
        Point pos = this.getPosition();
        Point wh = this.getExtent();
        g.setXORMode(Color.WHITE);
        g.drawOval(pos.x + dx, pos.y + dy, wh.x, wh.y);
    }

    public boolean pick(Point p, int dx, int dy){
        double a = (double)(this.getExtent().x / 2);
        double b = (double)(this.getExtent().y / 2);
        double x = (double)(p.x - dx - this.getCenter().x);
        double y = (double)(p.y - dy - this.getCenter().y);
        double r = Math.sqrt(x * x + y * y);
        double cosTheta = x / r;
        return (r * r < (b * b + cosTheta * cosTheta * (a * a - b * b)));
    }

    public Point getDrill(int dx, int dy){
        double a = (double)(this.getExtent().x / 2);
        double b = (double)(this.getExtent().y / 2);
        double hyp = Math.sqrt((double)(dy * dy + dx * dx));
        double cosTheta = dx / hyp;
        double sinTheta = dy / hyp;
        Point p = new Point();
        p.x = this.getCenter().x + (int)(a * cosTheta);
        p.y = this.getCenter().y + (int)(b * sinTheta);
        return p;
    }


}
