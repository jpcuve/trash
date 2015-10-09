package graphic.cities;

import graphic.cities.shp.ShpHandler;
import graphic.cities.xb.XBaseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Date;

public class EsriFile {
    private static final Logger LOG = LoggerFactory.getLogger(EsriFile.class);
    private final ShpHandler shpHandler = new ShpHandler() {
        public void fileHeader(int code, int length, int version, int type, Rectangle2D allxy, Rectangle2D allmz) {
        }

        public void record(int number, int length) {
        }

        public boolean bounds(int id, Rectangle2D bounds) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public void shape(int id, Shape shape, boolean fill) {
        }

        public void done() {
        }
    };
    private final XBaseHandler xBaseHandler = new XBaseHandler() {
        public void initTable(int version, Date updated, int recordCount, int headerLength, int recordLength, boolean incompleteTransaction, boolean encrypted, int mdx, int language) {
        }

        public void field(String name, char type, int length, int decimalCount, boolean index) {
        }

        public void doneDefinition() {
        }

        public void record(int id, boolean valid, String[] rec) {
        }

        public void doneTable() {
        }
    };

    public EsriFile(final String path) {
    }

}
