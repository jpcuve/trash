package lang.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Feb 11, 2005
 * Time: 10:56:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class Pool<E> implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pool.class);
    private ItemFactory<E> factory;
    private int maxActive;
    private int maxIdle;
    private long delay;
    private int pulse;
    private boolean open = true;
    private final Stack<E> items = new Stack<E>();
    private int borrowed = 0;
    private int count;

    public Pool(ItemFactory<E> factory, int maxActive, int minIdle, int maxIdle, long delay, int pulse) {
        this.factory = factory;
        this.maxActive = maxActive;
        this.maxIdle = maxIdle;
        this.delay = delay;
        this.pulse = this.count = pulse;
        for (int i = 0; i < minIdle; i++) items.push(factory.create());
        if (items.size() > 0) LOGGER.info("pool created items: " + minIdle + " (" + items.peek().getClass().getName() + ')');
        Thread thread = new Thread(this);
        thread.setName("poolcleaner-" + thread.getId());
        thread.start();
    }

    public int getMaxActive() {
        return maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public long getDelay() {
        return delay;
    }

    public int getPulse() {
        return pulse;
    }

    public void close() throws InterruptedException {
        open = false;
        LOGGER.debug("notifying pool");
        synchronized(this){
            notify();
        }
        LOGGER.debug("waiting for all items to be returned");
        synchronized(items){
            while (borrowed != 0) items.wait();
            for (E e: items) factory.destroy(e);
            if (items.size() > 0) LOGGER.info("pool destroyed items: " + items.size() + " (" + items.peek().getClass().getName() + ')');
            items.clear();
        }
    }

    public int getActiveCount(){
        synchronized(items){
            return borrowed;
        }
    }

    public int getIdleCount(){
        synchronized(items){
            return items.size();
        }
    }

    public void run() {
        try{
            while (open){
                // System.out.println("cleaning up");
                synchronized(items){
                    if (items.size() > maxIdle){
                        E e = items.pop();
                        factory.destroy(e);
                    }
                    if (pulse > 0){
                        count--;
                        if (count == 0){
                            for (E e: items) factory.pulse(e);
                            count = pulse;
                        }
                    }
                }
                // System.out.println("cleaned up");
                synchronized(this){
                    wait(delay);
                }
            }
        } catch(InterruptedException x){
            // ignore
        }
    }

    public E borrowItem() throws InterruptedException {
        synchronized(items){
            E e;
            if (items.size() > 0){
                e = items.pop();
            } else if (borrowed < maxActive){
                e = factory.create();
            } else{
                while (items.size() <= 0) items.wait();
                e = items.pop();
            }
            borrowed++;
            factory.activate(e);
            return e;
        }
    }

    public void returnItem(E e){
        synchronized(items){
            factory.passivate(e);
            items.push(e);
            borrowed--;
            items.notifyAll();
        }
    }
}
