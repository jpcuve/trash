package samples;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Mar 28, 2006
 * Time: 2:11:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadPool {
    public static class Executor extends Thread {
        private final ConcurrentLinkedQueue<Runnable> tasks;
        private boolean kill = false;

        public Executor(final ConcurrentLinkedQueue<Runnable> tasks) {
            this.tasks = tasks;
            this.setName("executor-" + getId());
        }

        public synchronized void kill(){
            this.kill = true;
            notify();
        }

        @Override
        public void run() {
            while (!(kill && tasks.isEmpty())){
                Runnable runnable = tasks.poll();
                if (runnable != null){
                    System.out.printf("%s executing %s%n", this, runnable);
                    runnable.run();
                }
            }
        }
    }

    public static void main(String[] args) {
        final ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<Runnable>();
        final Executor[] executors = new Executor[10];
        for (int i = 0; i < executors.length; i++){
            executors[i] = new ThreadPool.Executor(tasks);
            executors[i].start();
        }
        for (int i = 0; i < 100; i++) tasks.offer(new Runnable(){
            public void run() {
                try{
                    Thread.sleep((new Random().nextInt(5) + 1) * 1000);
                } catch(InterruptedException x){
                    // ignore
                }
            }
        });
        for (Executor executor : executors) executor.kill();
        for (Executor executor : executors) try{
            executor.join();
        } catch(InterruptedException x){
            // ignore
        }
        System.out.printf("finished%n");
    }

}
