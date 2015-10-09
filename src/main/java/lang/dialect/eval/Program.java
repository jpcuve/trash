package lang.dialect.eval;

import lang.dialect.Expression;
import lang.dialect.Interpreter;
import lang.dialect.terminal.Context;
import lang.dialect.terminal.Fault;
import lang.dialect.terminal.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lang.pool.ItemFactory;
import lang.pool.Pool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 16, 2006
 * Time: 5:15:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class Program extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(Program.class);
    private final LinkedBlockingQueue<EvaluationContext> queue = new LinkedBlockingQueue<EvaluationContext>();
    private final Library library;
    private Pool<Evaluator> pool;

    private class EvaluationContext{
        public Expression form;
        public EvalListener evalListener;

        public EvaluationContext(Expression form, EvalListener evalListener) {
            this.form = form;
            this.evalListener = evalListener;
        }
    }

    private class Evaluator extends Thread {
        private CountDownLatch killLatch = new CountDownLatch(1);
        private Interpreter interpreter = new Interpreter();

        public Evaluator(){
            setName("evaluator-" + getId());
        }

        public void kill(){
            killLatch.countDown();
            interrupt();
        }

        public void run() {
            while (killLatch.getCount() > 0){
                try{
                    EvaluationContext evaluationContext = queue.take();
                    Context environment = new Context(true, library);
                    long now = System.currentTimeMillis();
                    Expression eval = null;
                    try{
                        eval = interpreter.evaluate(evaluationContext.form, environment);
                    } catch(Exception x){
                        eval = new Fault(x);
                    }
                    EvalEvent event = new EvalEvent(this, 0, evaluationContext.form, eval , environment, System.currentTimeMillis() - now);
                    LOGGER.debug("evaluation: %s", event);
                    evaluationContext.evalListener.expressionEvaluated(event);
                } catch(InterruptedException x){
                    killLatch.countDown();
                }
            }
        }
    }

    private class EvaluatorFactory implements ItemFactory<Evaluator> {
        public Evaluator create() {
            Evaluator evaluator = new Evaluator();
            evaluator.start();
            return evaluator;
        }

        public void activate(Evaluator e) {
        }

        public void passivate(Evaluator e) {
        }

        public void destroy(Evaluator e) {
            e.kill();
        }

        public void pulse(Evaluator e) {
        }
    }

    public Program(Library library, int maxActive, int minIdle, int maxIdle, long delay) {
        this.library = library;
        this.pool = new Pool<Evaluator>(new EvaluatorFactory(), maxActive, minIdle, maxIdle, delay, -1);
    }

    public void evaluateLater(Expression eval, EvalListener evalListener) throws InterruptedException {
        queue.put(new EvaluationContext(eval, evalListener));
    }

    public void run() {
        pool.run();
    }

    public void kill(boolean immediate) throws InterruptedException {
        if (!immediate) while (queue.size() > 0) Thread.sleep(pool.getDelay());
        pool.close();
    }


}
