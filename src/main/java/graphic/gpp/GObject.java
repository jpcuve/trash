package graphic.gpp;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class GObject{
    public enum TextPosition{
        FILL, BOTTOM, TOP, LEFT, RIGHT
    }

    private String text;
    private TextPosition textPosition;
    private Point textExtent;
    private Color textColor;
    private Font textFont;
    private Color edge;
    private Color fill;
    private Point extent;
    private Point pos;

    public GObject(){
        this.setEdgeColor(Color.BLACK);
        this.setTextColor(Color.BLACK);
        this.setFillColor(null);
        this.setText(null);
        this.setTextPosition(TextPosition.FILL);
        this.setTextFont(Font.getFont("default"));
    }

    public GObject(int x, int y, int w, int h){
        this();
        this.setPosition(new Point(x, y));
        this.setExtent(new Point(w, h));
    }

    public void setText(String t){
        this.text = t;
    }

    public String getText(){
        return this.text;
    }

    public TextPosition getTextPosition() {
        return textPosition;
    }

    public void setTextPosition(TextPosition textPosition) {
        this.textPosition = textPosition;
    }

    public void setTextExtent(Point wh){
        this.textExtent = wh;
    }

    public Point getTextExtent(){
        return this.textExtent;
    }

    public void setTextColor(Color c){
        this.textColor = c;
    }

    public Color getTextColor(){
        return this.textColor;
    }

    public void setTextFont(Font f){
        this.textFont = f;
    }

    public Font getTextFont(){
        return this.textFont;
    }

    public void setEdgeColor(Color c){
        this.edge = c;
    }

    public Color getEdgeColor(){
        return this.edge;
    }

    public void setFillColor(Color c){
        this.fill = c;
    }

    public Color getFillColor(){
        return this.fill;
    }

    public void setColor(Color c){
        this.setEdgeColor(c);
        this.setFillColor(c);
        this.setTextColor(c);
    }

    public void setPosition(Point pos){
        this.pos = pos;
    }

    public Point getPosition(){
        return this.pos;
    }

    public void setExtent(Point wh){
        this.extent = wh;
    }

    public Point getExtent(){
        return this.extent;
    }

    // drawing methods

    public void draw(Graphics g){
        this.draw(g, 0, 0);
    }

    public void draw(Graphics g, int dx, int dy){
        this.drawShape(g, dx, dy);
        this.drawText(g);
    }

    public void draw(Graphics g, Rectangle clipRect){
        this.draw(g, 0, 0, clipRect);
    }

    public void draw(Graphics g, int dx, int dy, Rectangle clipRect){
        Point pos = this.getPosition();
        Point wh = this.getExtent();
        Rectangle r = new Rectangle(pos.x + dx, pos.y + dy, wh.x, wh.y);
        if(clipRect.intersects(r)) this.draw(g, dx, dy);
    }

    public abstract void drawShape(Graphics g, int dx, int dy);


    //

    protected Rectangle getTextRectangle(){
        String s = this.getText();
        Point p = this.getTextExtent();
        if(s != null && p != null){
            Point pc = this.getCenter();
            Point pos = this.getPosition();
            Point wh = this.getExtent();
            switch(textPosition){
                case FILL:
                    return new Rectangle(pc.x - p.x / 2, pc.y - p.y / 2, p.x, p.y);
                case BOTTOM:
                    return new Rectangle(pc.x - p.x / 2, pos.y + wh.y + 1, p.x, p.y);
                case TOP:
                    return new Rectangle(pc.x - p.x / 2, pos.y - p.y, p.x, p.y);
                case LEFT:
                    return new Rectangle(pc.x - p.x, pos.y, p.x, p.y);
                case RIGHT:
                    return new Rectangle(pc.x + wh.x, pos.y, p.x, p.y);
            }
        }
        return null;
    }


    public void drawText(Graphics g){
        if(text != null){
            g.setFont(textFont);
            FontMetrics fontMetrics = g.getFontMetrics(textFont);
            Rectangle2D d = fontMetrics.getStringBounds(text, g);
            Point p = new Point((int)d.getWidth(), (int)d.getHeight());
            this.setTextExtent(p);
            Rectangle rt = this.getTextRectangle();
            g.setColor(textColor);
            g.drawString(text, rt.x, rt.y);
        }
    }

    public Point getCenter(){
        Point pos = this.getPosition();
        Point wh = this.getExtent();
        return new Point(pos.x + wh.x / 2, pos.y + wh.y / 2);
    }

    public Rectangle getOutline(){
        Point pos = this.getPosition();
        Point wh = this.getExtent();
        return new Rectangle(pos.x, pos.y, wh.x, wh.y);
    }

    public void offset(int dx, int dy){
        Point p = this.getPosition();
        p.x += dx;
        p.y += dy;
    }

    public void invalidate(Component ctl){
        Point pos = this.getPosition();
        Point wh = this.getExtent();
        Rectangle r = new Rectangle(pos.x, pos.y, wh.x, wh.y);
        Rectangle rt = this.getTextRectangle();
        if(rt != null) r = r.union(rt);
        r.width++;
        r.height++;
        ctl.repaint(r.x, r.y, r.width, r.height);
    }

    public Point getDrill(int dx, int dy){
        Point pos = this.getPosition();
        Point wh = this.getExtent();
        Point p = new Point();
        int x = 0;
        int y = 0;
        int absx = Math.abs(dx);
        int absy = Math.abs(dy);
        if((double)absx / (double)absy > (double)wh.x / (double)wh.y){
            if(dx > 0){
                p.x = pos.x + wh.x;
                p.y = pos.y + wh.y / 2 + (wh.x * dy) / 2 / dx;
            }else if(dx < 0){
                p.x = pos.x;
                p.y = pos.y + wh.y / 2 - (wh.x * dy) / 2 / dx;
            }
        }else{
            if(dy > 0){
                p.x = pos.x + wh.x / 2 + (wh.y * dx) / 2 / dy;
                p.y = pos.y + wh.y;
            }else if (dy < 0){
                p.x = pos.x + wh.x / 2 - (wh.y * dx) / 2 / dy;
                p.y = pos.y;
            }
        }
        return p;
    }

}
