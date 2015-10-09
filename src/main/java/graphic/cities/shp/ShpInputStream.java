package graphic.cities.shp;

import graphic.cities.BinaryInputStream;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 2, 2010
 * Time: 2:32:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShpInputStream extends BinaryInputStream {

    public ShpInputStream(InputStream is) {
        super(is);
    }

    public int readIntBig() throws IOException {
        long l = readLong(4, false);
        return (int) (l >= 0x80000000 ? l - 0x100000000L : l);
    }

    public int readIntLit() throws IOException {
        long l = readLong(4, true);
        return (int) (l >= 0x80000000 ? l - 0x100000000L : l);
    }

    public double readDblBig() throws IOException {
        long l = readLong(8, false);
        return Double.longBitsToDouble(l);
    }

    public double readDblLit() throws IOException {
        long l = readLong(8, true);
        return Double.longBitsToDouble(l);
    }

    public Point2D.Double readPoint() throws IOException {
        double x = readDblLit();
        double y = readDblLit();
        return new Point2D.Double(x, y);
    }

    public Rectangle2D.Double readBox() throws IOException {
        double xmin = readDblLit();
        double ymin = readDblLit();
        double xmax = readDblLit();
        double ymax = readDblLit();
        return new Rectangle2D.Double(xmin, ymin, xmax - xmin, ymax - ymin);
    }

}
