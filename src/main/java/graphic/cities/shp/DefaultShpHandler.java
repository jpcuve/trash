package graphic.cities.shp;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 2, 2010
 * Time: 2:47:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultShpHandler implements ShpHandler {
    public void fileHeader(int code, int length, int version, int type, Rectangle2D allxy, Rectangle2D allmz) {
        System.out.printf("file code: %s, file length: %s, version: %s, shape type: %s, allxy: %s, allmz: %s%n", code, length, version, type, allxy, allmz);
    }

    public void record(int number, int length) {
        System.out.printf("record number: %s, length: %s%n", number, length);
    }

    public boolean bounds(int id, Rectangle2D bounds) {
        return false;
    }

    public void shape(int id, Shape shape, boolean fill) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void done() {
        System.out.printf("done%n");
    }
}
