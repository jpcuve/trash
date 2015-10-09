package graphic.cities.shp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class JShapePanel extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(JShapePanel.class);
    public static final Dimension PREFERRED_SIZE = new Dimension(400, 300);
    private final String filename;
    private AffineTransform at;

    public JShapePanel(final String filename) {
        this.filename = filename;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        final int width = getWidth();
        final int height = getHeight();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);
        ShpInputStream sis = null;
        try{
            sis = new ShpInputStream(new FileInputStream(filename));
            final ShpParser parser = new ShpParser(sis);
            parser.parse(new ShpHandler(){
                private int shapeCount;
                private double mx;
                private double my;

                public void fileHeader(int code, int length, int version, int type, Rectangle2D allxy, Rectangle2D allmz) {
                    shapeCount = 0;
                    mx = width / allxy.getWidth();
                    my = height / allxy.getHeight();
                    at = new AffineTransform(mx, 0, 0,  -my , -allxy.getX() * mx, allxy.getY() * my + height);
                    g2d.setTransform(at);
                    g2d.setStroke(new BasicStroke(0));
                }

                public void record(int number, int length) {
                }

                public boolean bounds(int id, Rectangle2D bounds) {
                    shapeCount++;
                    return !(bounds.getWidth() * mx == 0 && bounds.getHeight() * my == 0);
                }

                public void shape(int id, Shape shape, boolean fill) {
                    g2d.draw(shape);
                }

                public void done() {
                    LOG.debug("read shape count: {}", shapeCount);
                }
            });
        } catch(IOException x){
            x.printStackTrace();
            LOG.error("error parsing file: " + filename, x);

        } finally{
            if (sis != null) try{
                sis.close();
            } catch(IOException x){
                LOG.error("cannot close shape stream", x);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return PREFERRED_SIZE;
    }

    public static void main(String[] args) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("");
        logger.setLevel(Level.FINE);
        for (Handler handler: logger.getHandlers()){
            handler.setLevel(Level.FINE);
            handler.setFormatter(new Formatter(){
                @Override
                public String format(LogRecord logRecord) {
                    return String.format("%s%n", logRecord.getMessage());
                }
            });
        }
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new JShapePanel("eur/bnd-political-boundary-a.shp"));
        f.pack();
        f.setVisible(true);
    }
}
