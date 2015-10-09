package lang.dialect;

import lang.dialect.eval.EvalEvent;
import lang.dialect.eval.EvalListener;
import lang.dialect.terminal.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 29, 2004
 * Time: 4:40:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Interpreter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Interpreter.class);
    private List<EvalListener> listeners = null;
    private Stack<Expression> forms = new Stack<Expression>();
    private Stack<Context> environments = new Stack<Context>();

    public int getLevel() {
        return forms.size();
    }

    public Expression getForm(){
        return forms.size() > 0 ? (Expression)forms.peek() : null;
    }

    public Context getEnvironment(){
        return environments.size() > 0 ? (Context)environments.peek() : null;
    }

    public Expression evaluate(Expression form, Context environment) throws DialectException {
        forms.push(form);
        environments.push(environment);
        evaluate();
        environments.pop();
        return forms.pop();
    }

    public Expression evaluate(Expression form) throws DialectException {
        forms.push(form);
        environments.push(environments.peek());
        evaluate();
        environments.pop();
        return forms.pop();
    }

    public void evaluate() throws DialectException {
        Expression in = forms.peek();
        LOGGER.debug("entering evaluation loop, level: %d, form= %s", getLevel(), forms.peek());
        long now = System.currentTimeMillis();
        while((forms.peek()).reduce(this)) LOGGER.debug("reducing...");
        LOGGER.debug("exiting evaluation loop, level: %d, form= %s", getLevel(), forms.peek());
        fireExpressionEvaluated(forms.size(), in, forms.peek(), environments.peek(), System.currentTimeMillis() - now);
    }

    public void substitute(Expression form){
        forms.pop();
        forms.push(form);
    }

    public void substitute(Expression form, Context environment){
        forms.pop();
        environments.pop();
        environments.push(environment);
        forms.push(form);
    }

    public void pop(){
        forms.pop();
        environments.pop();
    }

    public void addEvalListener(EvalListener listener){
        if (listeners == null) listeners = new ArrayList<EvalListener>();
        listeners.add(listener);
    }

    public void removeEvalListener(EvalListener listener){
        if (listeners != null) listeners.remove(listener);
    }

    private void fireExpressionEvaluated(int level, Expression form, Expression eval, Context env, long executionTime){
        EvalEvent event = new EvalEvent(this, level, form, eval, env, executionTime);
        LOGGER.debug("evaluation event: " + event);
        if (listeners != null) for (EvalListener listener : listeners) listener.expressionEvaluated(event);
    }

}
