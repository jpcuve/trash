package graphic.cities.shp;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * User: jpc
 * Date: Dec 2, 2010
 * Time: 2:46:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShpParser {
    private final ShpInputStream sis;

    public ShpParser(ShpInputStream sis) {
        this.sis = sis;
    }

    public void parse(final ShpHandler handler) throws IOException {
        int fileCode = sis.readIntBig();
        sis.skip(20);
        int fileLength = sis.readIntBig();
        int version = sis.readIntLit();
        int type = sis.readIntLit();
        final Rectangle2D xy = sis.readBox();
        final Rectangle2D zm = sis.readBox();
        handler.fileHeader(fileCode, fileLength * 2, version, type, xy, zm);
        int remaining = fileLength * 2 - 100;
        while (remaining > 0){
            int rec = sis.readIntBig();
            int size = sis.readIntBig();
            handler.record(rec, size);
            switch(type){
                case 1:{
                    int shape = sis.readIntLit();
                    final Point2D.Double point = sis.readPoint();
                    if (handler.bounds(shape, new Rectangle2D.Double(point.getX(), point.getY(), 0, 0))){

                    };
                    break;
                }
                case 3:
                case 5:{
                    int shape = sis.readIntLit();
                    final Rectangle2D bounds = sis.readBox();
                    int numParts = sis.readIntLit();
                    int numPoints = sis.readIntLit();
                    if(handler.bounds(shape, bounds)){
                        int[] parts = new int[numParts];
                        for (int i = 0; i < parts.length; i++) parts[i] = sis.readIntLit();
                        Point2D.Double[] points = new Point2D.Double[numPoints];
                        for (int i = 0; i < points.length; i++) points[i] = sis.readPoint();
                        for (int i = 0; i < parts.length; i++){
                            final Path2D.Double path = new Path2D.Double();
                            path.moveTo(points[parts[i]].getX(), points[parts[i]].getY());
                            for (int j = parts[i] + 1; j < (i < parts.length - 1 ? parts[i + 1] : points.length - 1); j++){
                                path.lineTo(points[j].getX(), points[j].getY());
                            }
                            handler.shape(shape, path, shape == 5);
                        }
                    } else{
                        sis.skip(numParts * 4 + numPoints * 16);
                    }
                    break;
                }
                default:
                    throw new IOException("unrecognized shape: " + type);

            }
            remaining -= (size + 4) * 2;
        }
        handler.done();
    }

    public static void main(String[] args) throws Exception {
        final ShpInputStream sis = new ShpInputStream(new FileInputStream("eur/bnd-political-boundary-a.shp"));
        final ShpParser parser = new ShpParser(sis);
        parser.parse(new DefaultShpHandler());
        sis.close();
    }

}
