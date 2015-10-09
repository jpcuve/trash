/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 17, 2002
 * Time: 12:08:41 PM
 * To change this template use Options | File Templates.
 */
package swing.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JLed extends JPanel implements MouseListener {
    private static int D = 10;
    private Image on;
    private Image off;
    private Image dis;
    private boolean state;
    private String message;

    public JLed(){
        state = false;
        this.addMouseListener(this);
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Dimension getMinimumSize() {
        return new Dimension(D, D);
    }

    public Dimension getPreferredSize() {
        return new Dimension(D, D);
    }

    public void render(Graphics2D g2d, Color c){
        g2d.setColor(this.getBackground());
        g2d.fillRect(0, 0, D, D);
        g2d.setColor(c);
        g2d.fillOval(0, 0, D, D);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawArc(0, 0, D, D, 30, 210);
        g2d.setColor(Color.WHITE);
        g2d.drawArc(0, 0, D, D, 240, 150);
        g2d.drawArc(1, 1, D - 2, D - 2, 120, 30);
    }

    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        if (on == null){
            on = g2d.getDeviceConfiguration().createCompatibleImage(D, D);
            this.render((Graphics2D)on.getGraphics(), this.getForeground());
            off = g2d.getDeviceConfiguration().createCompatibleImage(D, D);
            this.render((Graphics2D)off.getGraphics(), Color.BLACK);
            dis = g2d.getDeviceConfiguration().createCompatibleImage(D, D);
            this.render((Graphics2D)dis.getGraphics(), this.getBackground());
        }
        if (this.isEnabled()){
            if (state){
                g.drawImage(on, 0, 0, this);
            } else{
                g.drawImage(off, 0, 0, this);
            }
        } else{
            g.drawImage(dis, 0, 0, this);
        }
    }

    public void mouseClicked(MouseEvent event) {
        state = !state;
        this.repaint();
    }

    public void mousePressed(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }
}
