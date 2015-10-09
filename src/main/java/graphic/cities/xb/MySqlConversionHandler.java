package graphic.cities.xb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 3, 2010
 * Time: 5:21:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class MySqlConversionHandler implements XBaseHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MySqlConversionHandler.class);
    private static final String NL = String.format("%n");
    private final String tableName;
    private final Writer writer;
    private final Map<String, Character> types = new LinkedHashMap<String, Character>();
    private boolean commaField = false;
    private boolean commaRecord = false;


    public MySqlConversionHandler(String tableName, Writer writer) {
        this.tableName = tableName;
        this.writer = writer;
    }

    private void output(final String s){
        output(s, true);
    }

    private void output(final String s, boolean nl){
        try{
            writer.write(String.format("%s", s));
            if (nl) writer.write(NL);
        } catch(IOException x){
            LOG.error("cannot write", x);
        }
    }

    public void initTable(int version, Date updated, int recordCount, int headerLength, int recordLength, boolean incompleteTransaction, boolean encrypted, int mdx, int language) {
        output(String.format("drop table if exists %s;", tableName));
        output(String.format("create table %s (", tableName));
    }

    public void field(String name, char type, int length, int decimalCount, boolean index) {
        types.put(name, type);
        output(commaField ? " ," : "  ", false);
        switch(type){
            case 'C':
                output(String.format("%s varchar(%s)", name, length));
                commaField = true;
                break;
            case 'N':
                if (decimalCount == 0){
                    output(String.format("%s int(%s)", name, length));
                } else{
                    output(String.format("%s decimal(%s,%s)", name, length, decimalCount));
                }
                commaField = true;
                break;
        }
    }

    private String sql(char type, String val){
        switch(type){
            case 'N':
                return val;
        }
        return String.format("'%s'", val.replace("'","''"));
    }

    public void doneDefinition() {
        output(String.format(");"));
        output(String.format("insert into %s(", tableName), false);
        boolean comma = false;
        for (final String fieldName: types.keySet()){
            if (comma) output(",", false);
            output(fieldName, false);
            comma = true;
        }
        output(") values");
    }

    public void record(int id, boolean valid, String[] rec) {
        output(commaRecord ? " ,(" : "  (", false);
        boolean comma = false;
        int i = 0;
        for (final String fieldName: types.keySet()){
            if (comma) output(",", false);
            output(sql(types.get(fieldName), rec[i]), false);
            comma = true;
            i++;
        }
        output(")");
        commaRecord = true;
    }

    public void doneTable() {
        output(String.format(";"));
        try{
            writer.flush();
        } catch(IOException x){
            LOG.error("cannot flush", x);
        }
    }
}
