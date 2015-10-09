/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Feb 5, 2003
 * Time: 10:08:40 AM
 * To change this template use Options | File Templates.
 */
package samples;

import java.net.InetAddress;
import java.util.Date;

public class Tz {
    public static void main(String[] args)
    throws Exception{
        Date d = new Date();
        System.out.println("Date=" + d);
        System.out.println("Ms=" + d.getTime());
        InetAddress ia = InetAddress.getLocalHost();
        System.out.println("Local=" + ia.getHostName());
    }
}
