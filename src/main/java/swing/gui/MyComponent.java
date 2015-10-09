package swing.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jun 1, 2005
 * Time: 12:17:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyComponent extends JComponent {
    private static final Dimension PREFERRED_SIZE = new Dimension(80, 60);
    public int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        g.drawLine(0, 0, w, h);
        g.drawLine(0, h, w, 0);
        g.drawOval(0, 0, w, h);
    }

    public Dimension getPreferredSize() {
        return PREFERRED_SIZE;
    }
}
