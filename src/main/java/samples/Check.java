/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 23, 2004
 * Time: 5:24:21 PM
 * To change this template use Options | File Templates.
 */
package samples;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Socket;

public class Check {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("10.1.1.134", 12345);
        InputStream is = socket.getInputStream();
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));
        String s = reader.readLine();
        System.out.println("s=" + s);
        is.close();
        socket.close();

    }
}
