package samples;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Feb 13, 2006
 * Time: 11:25:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class Timer {
    public static void main(String[] args) throws Exception {
        while(true){
            long now = System.currentTimeMillis();
            CountDownLatch latch = new CountDownLatch(1);
            System.out.printf("waiting 1 sec%n");
            latch.await(1000, TimeUnit.MILLISECONDS);
            System.out.printf("waited 1 sec%n");
            long delay = System.currentTimeMillis() - now;
            if (delay > 2000) System.out.printf("delay > 2: %s%n", delay);
        }
    }
}
