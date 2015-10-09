package samples;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 7, 2004
 * Time: 3:02:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadTest extends Thread {
    public void run() {
        System.out.println("running thread=" + this);
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadTest threadTest = new ThreadTest();
        threadTest.start();
        threadTest.join();
        threadTest.start();
        threadTest.join();
        threadTest.start();
        threadTest.join();
        threadTest.start();
        threadTest.join();
        threadTest.start();
        threadTest.join();
        threadTest.start();
        threadTest.join();
        threadTest.start();
    }
}
