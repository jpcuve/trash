package graphic.gpp;

import java.awt.*;
import java.util.EventObject;

public class GEvent extends EventObject{

    private int dx;
    private int dy;

    public GEvent(Object source){
        this(source, 0, 0);
    }

    public GEvent(Object source, int dx, int dy){
        super(source);
        this.dx = dx;
        this.dy = dy;
    }

    public Point getOffset(){
        return new Point(dx, dy);
    }

}
