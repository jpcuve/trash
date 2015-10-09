package lang.dialect.eval;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 30, 2004
 * Time: 11:52:28 AM
 * To change this template use File | Settings | File Templates.
 */
public interface EvalListener extends EventListener {
    void expressionEvaluated(EvalEvent e);
}
