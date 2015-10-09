package graphic.cities.shp;

import java.awt.*;
import java.awt.geom.Rectangle2D;


/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 2, 2010
 * Time: 2:46:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ShpHandler {
    void fileHeader(int code, int length, int version, int type, Rectangle2D allxy, Rectangle2D allmz);
    void record(int number, int length);
    boolean bounds(int id, Rectangle2D bounds);
    void shape(int id, Shape shape, boolean fill);
    void done();
}
