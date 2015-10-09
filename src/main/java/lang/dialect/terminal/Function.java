package lang.dialect.terminal;

import lang.dialect.DialectException;
import lang.dialect.Expression;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 27, 2004
 * Time: 12:33:13 PM
 * To change this template use File | Settings | File Templates.
 */

public interface Function {
     Expression apply(List<Expression> arguments, int level, Context environment) throws DialectException;
}
