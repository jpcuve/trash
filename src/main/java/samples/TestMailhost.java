/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: 10-Jun-02
 * Time: 11:56:28
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package samples;

import java.net.InetAddress;

public class TestMailhost {
    public static void main(String[] args)
    throws Exception {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }
}
