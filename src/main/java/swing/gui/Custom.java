package swing.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 2, 2005
 * Time: 6:16:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class Custom extends JComponent {
    protected void paintComponent(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        g.drawLine(0, 0, w, h);
        g.drawLine(w, 0, 0, h);
    }
}
