/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 17, 2002
 * Time: 3:17:13 PM
 * To change this template use Options | File Templates.
 */
package swing.controller;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class JPowerSlider extends JTextField implements MouseListener, MouseMotionListener, ChangeListener, ActionListener {
    private int min;
    private int max;
    private int lastX;
    private int lastY;

    private JWindow w;
    private JSlider s;

    public JPowerSlider(){
        w = new JWindow();
        s = new JSlider(JSlider.HORIZONTAL, 0, 100, 25);
        s.setBorder(BorderFactory.createLineBorder(Color.black));
        s.addChangeListener(this);
        w.getContentPane().add(s);
        w.pack();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addActionListener(this);
        this.stateChanged(null);
    }

    /*
    public Dimension getPreferredSize() {
        return new Dimension(20, 20);
    }

    public void paintComponent(Graphics minorVersion){
        Graphics2D g2d = (Graphics2D)minorVersion;
        Dimension productId = this.getSize();
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(0, 0, productId.width - 1, productId.height - 1);
    }
    */

    public void mouseClicked(MouseEvent event) {
    }

    public void mousePressed(MouseEvent event) {
        Point p = this.getLocationOnScreen();
        w.setLocation(event.getX() + (int)p.getX(), event.getY() + (int)p.getY());
        lastX = event.getX();
        lastY = event.getY();
        w.show();
    }

    public void mouseReleased(MouseEvent event) {
        w.hide();
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mouseDragged(MouseEvent event) {
        if (w.isShowing()){
            int dx = event.getX() - lastX;
            int dy = event.getY() - lastY;
            if (s.getOrientation() == JSlider.VERTICAL) s.setValue(s.getValue() - dy);
            else s.setValue(s.getValue() + dx);
            lastX = event.getX();
            lastY = event.getY();
        }
    }

    public void mouseMoved(MouseEvent event) {
    }

    public void stateChanged(ChangeEvent event) {
        this.setText(Integer.toString(s.getValue()));
    }

    public void actionPerformed(ActionEvent event) {
        s.setValue(Integer.parseInt(this.getText()));
    }
}
