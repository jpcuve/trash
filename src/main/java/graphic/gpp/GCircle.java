package graphic.gpp;

import java.awt.*;

public class GCircle extends GEllipse{

    public GCircle(int x, int y, int radius){
        super(x, y, radius * 2, radius * 2);
    }

    public boolean pick(Point p){
        int x = p.x - this.getCenter().x;
        int a = this.getExtent().x / 2;
        return (x * x < a * a);
    }
}
