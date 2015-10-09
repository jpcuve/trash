package graphic.gpp;

import java.awt.*;

public class GTriangle extends GShape{

    public GTriangle(int x, int y, int w, int h){
        super(x, y, w, h);
        this.setTextPosition(TextPosition.BOTTOM);
    }

    public void drawShape(Graphics g, int dx, int dy){
        Point pos = this.getPosition();
        Point wh = this.getExtent();
        int[] px = new int[3];
        int[] py = new int[3];
        px[0] = pos.x + dx; py[0] = pos.y + wh.y + dy;
        px[1] = pos.x + wh.x / 2 + dx; py[1] = pos.y + dy;
        px[2] = pos.x + wh.x + dx; py[2] = pos.y + wh.y + dy;
        g.drawPolygon(px, py, 3);
    }

    public void outlineShape(Graphics g, int dx, int dy){
        Point pos = this.getPosition();
        Point wh = this.getExtent();
        int[] px = new int[3];
        int[] py = new int[3];
        px[0] = pos.x + dx; py[0] = pos.y + wh.y + dy;
        px[1] = pos.x + wh.x / 2 + dx; py[1] = pos.y + dy;
        px[2] = pos.x + wh.x + dx; py[2] = pos.y + wh.y + dy;
        g.setXORMode(Color.WHITE);
        g.drawPolygon(px, py, 3);
    }

    public boolean pick(Point p, int dx, int dy){
        Rectangle r = this.getOutline();
        r.x += dx;
        r.y += dy;
        return r.contains(p);
    }

}
