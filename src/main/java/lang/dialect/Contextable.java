package lang.dialect;

import lang.dialect.terminal.Context;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 7, 2005
 * Time: 1:16:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Contextable {
    void fromContext(Context c);
    void toContext(Context c);
}
