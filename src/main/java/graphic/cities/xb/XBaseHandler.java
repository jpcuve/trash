package graphic.cities.xb;

import java.util.Date;

public interface XBaseHandler {
    void initTable(int version, Date updated, int recordCount, int headerLength, int recordLength, boolean incompleteTransaction, boolean encrypted, int mdx, int language);
    void field(String name, char type, int length, int decimalCount, boolean index);
    void doneDefinition();
    void record(int id, boolean valid, String[] rec);
    void doneTable();
}
