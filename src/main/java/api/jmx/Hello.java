package api.jmx;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 30, 2004
 * Time: 2:32:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class Hello extends NotificationBroadcasterSupport implements HelloMBean {
    private String name = "Reginald";
    private int cacheSize = 200;
    private int sequenceNumber = 0;

    public void sayHello() {
        System.out.println("Hello, world");
    }

    public int add(int x, int y) {
        return x + y;
    }

    public String getName() {
        return name;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        int oldValue = cacheSize;
        this.cacheSize = cacheSize;
        Notification notification = new AttributeChangeNotification(this, ++sequenceNumber, System.currentTimeMillis() , "cache size changed", "cacheSize", "int", oldValue, cacheSize);
        sendNotification(notification);
    }

    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE };
        String name = AttributeChangeNotification.class.getName();
        String description = "An attribute of this MBean has changed";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description);
        return new MBeanNotificationInfo[] {info};
    }

    public static void main(String[] args) throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        mBeanServer.registerMBean(new Hello(), new ObjectName("jmx:type=Hello"));
        Thread.sleep(Long.MAX_VALUE);
    }
}
