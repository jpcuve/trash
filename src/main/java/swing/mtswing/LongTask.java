package swing.mtswing;

import javax.swing.event.EventListenerList;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class LongTask implements Callable<String> {
    private CountDownLatch latch = new CountDownLatch(1);
    private EventListenerList listeners = new EventListenerList();

    public String call() throws Exception {
        fireMessageEvent(String.format("%s: started", Thread.currentThread().getName()));
        int l = 0;
        while (latch.getCount() > 0 && l < 10) try {
            Thread.sleep(1000);
            fireMessageEvent(String.format("%s: %s", Thread.currentThread().getName(), l));
            l++;
        } catch(InterruptedException x){
            fireMessageEvent(String.format("%s: interrupted", Thread.currentThread().getName()));
            latch.countDown();
        }
        fireMessageEvent(String.format("%s: stopped", Thread.currentThread().getName()));
        return "terminated";
    }

    protected void fireMessageEvent(final String message){
        final Object[] ls = listeners.getListenerList();
        for (int i = ls.length-2; i >= 0; i -= 2) if (ls[i] ==MessageListener.class) ((MessageListener) ls[i+1]).messageSent(new MessageEvent(this, message));
    }

    public void addMessageListener(final MessageListener listener){
        listeners.add(MessageListener.class, listener);
    }

    public void removeMessageListener(final MessageListener listener){
        listeners.remove(MessageListener.class, listener);
    }
}
