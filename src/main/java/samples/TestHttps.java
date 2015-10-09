package samples;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Sep 8, 2004
 * Time: 1:23:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestHttps {
    public static final String TARGET = "https://www.paypal.com";
    public static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        URL url = new URL(TARGET);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        int count = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = connection.getInputStream().read(buffer)) != -1) count += read;
        System.out.printf("%s bytes read\n", count);
        connection.getInputStream().close();
    }
}
