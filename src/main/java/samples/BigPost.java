package samples;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Apr 12, 2006
 * Time: 10:04:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class BigPost {
    public static void main(String[] args) throws Exception {
        System.getProperties().setProperty("javax.net.ssl.trustStore", "truststore.ks");
        System.getProperties().setProperty("javax.net.ssl.trustStorePassword", "totalwide");
        final URL url = new URL("https://jean-pierre.asusserver.transwide.com:8443/railion/incoming");
        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setHostnameVerifier(new HostnameVerifier(){
            public boolean verify(String string, SSLSession sslSession) {
                return true;
            }
        });
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        final OutputStream os = connection.getOutputStream();
        try{
            for (int i = 0; i < 800000; i++) os.write(i);
            Thread.sleep(180000);
        } finally{
            os.close();
        }
        final InputStream is = connection.getInputStream();
        int b;
        try{
            while ((b = is.read()) != -1) System.out.print((char)b);
        } finally{
            is.close();
        }
        connection.disconnect();
    }
}
