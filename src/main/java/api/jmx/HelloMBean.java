package api.jmx;

import javax.management.NotificationBroadcaster;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 30, 2004
 * Time: 2:30:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HelloMBean extends NotificationBroadcaster {
    void sayHello();
    int add(int x, int y);
    String getName();
    int getCacheSize();
    void setCacheSize(int cacheSize);
}
