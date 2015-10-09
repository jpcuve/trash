/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 17, 2002
 * Time: 10:18:58 AM
 * To change this template use Options | File Templates.
 */
package swing.controller;

import javax.swing.*;
import java.awt.*;

public class JKnob extends JPanel {
    public static final int D = 30;

    public Dimension getPreferredSize() {
        return new Dimension(D, D);
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        Image im = g2d.getDeviceConfiguration().createCompatibleImage(D, D);
        Graphics2D gi = (Graphics2D)im.getGraphics();
        gi.setColor(this.getBackground());
        gi.fillRect(0, 0, D, D);
        gi.setColor(this.getForeground());
        gi.fillOval(0, 0, D, D);
        gi.setColor(Color.GRAY);
        gi.fillOval(0 + D / 10, 0 + D / 10, D -  D / 5, D -  D / 5);
        g2d.drawImage(im, 0, 0, this);
    }
}
