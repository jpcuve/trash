package lang.dialect.eval;

import lang.dialect.Expression;
import lang.dialect.terminal.Context;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 30, 2004
 * Time: 11:51:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class EvalEvent extends EventObject {
    private int level;
    private Expression form;
    private Expression eval;
    private Context env;
    private long executionTime;

    public EvalEvent(Object o, int level, Expression form, Expression eval, Context env, long executionTime) {
        super(o);
        this.level = level;
        this.form = form;
        this.eval = eval;
        this.env = env;
        this.executionTime = executionTime;
    }

    public int getLevel() {
        return level;
    }

    public Expression getForm() {
        return form;
    }

    public Expression getEval() {
        return eval;
    }

    public Context getEnvironment(){
        return env;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public String toString(){
        return form.getTag() + ":" + level + "> " + form + " ~ " + eval + ", " + executionTime + "ms, env: " + env.entrySet().size();
    }
}
