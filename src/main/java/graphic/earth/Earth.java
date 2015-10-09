package graphic.earth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Math.*;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 21, 2005
 * Time: 1:59:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class Earth extends JComponent implements MouseListener, MouseMotionListener {
    private static final Dimension PREFERRED_SIZE = new Dimension(220, 180);
    private double rx = 0;
    private double ry = 0;
    private static final double[][] AFRICA = {{10, 0}, {20, -35}, {40, -20}, {40, 0}, {50, 15}, {45, 15}, {30, 30}, {0, 35}, {-20, 20}, {-10, 5}};
    private List<Position> shape  = new ArrayList<Position>();

    private class Position{
        public double longitude;
        public double latitude;

        public Position(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }

    public Earth() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        for (double[] point: AFRICA) shape.add(new Position(point[0] * PI / 180, -point[1] * PI / 180));
    }

    public void paint(Graphics g) {
        int cx = this.getWidth() / 2;
        int cy = this.getHeight() / 2;
        int r = cx < cy ? cx : cy;
        g.drawOval(cx - r, cy - r, 2 * r, 2 * r);



        int[] x = new int[shape.size()];
        int[] y = new int[shape.size()];
        int count = 0;
        for (Position position: shape){
            double xx = cos(ry) * sin(position.longitude) - sin(ry) * (sin(rx) * cos(position.longitude) * sin(position.latitude) + cos(rx) * cos(position.longitude) * cos(position.latitude));
            double yy = cos(rx) * cos(position.longitude) * sin(position.latitude) - sin(rx) * cos(position.longitude) * cos(position.latitude);
            double zz = sin(ry) * sin(position.longitude) + cos(ry) * (sin(rx) * cos(position.longitude) * sin(position.latitude) + cos(rx) * cos(position.longitude) * cos(position.latitude));
            int dx = (int)round(r * xx);
            int dy = (int)round(r * yy);
            double factor = zz > 0 ? 1 : r / sqrt(dx * dx + dy * dy);
            g.drawLine(cx + dx - 1, cy + dy - 1, cx + dx + 1, cy + dy + 1);
            g.drawLine(cx + dx - 1, cy + dy + 1, cx + dx + 1, cy + dy - 1);
            x[count] = cx + (int)round(r * xx * factor);
            y[count] = cy + (int)round(r * yy * factor);
            count++;
        }
        boolean visible = true;
        if (visible) g.drawPolygon(x, y, count);
    }

    public Dimension getPreferredSize() {
        return PREFERRED_SIZE;
    }

    private int mousex;
    private int mousey;

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        mousex = e.getX();
        mousey = e.getY();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        ry -= (e.getX() - mousex) * Math.PI / 180;
        rx -= (e.getY() - mousey) * Math.PI / 180;
        mousex = e.getX();
        mousey = e.getY();
        this.repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public static void main(String[] args) {
        System.out.println(new SimpleDateFormat("yyyymmDD-HH").format(new Date(1114775173590L)));
        JFrame jFrame = new JFrame();
        jFrame.add(new Earth());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
