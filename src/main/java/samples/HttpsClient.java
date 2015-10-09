/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 20, 2004
 * Time: 10:40:22 AM
 * To change this template use Options | File Templates.
 */
package samples;

import sun.misc.BASE64Encoder;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.Certificate;

public class HttpsClient {
    public static final String DESTINATION = "https://jpc.asusserver.transwide.com:8443/https-server";
    public static final String USERNAME = "superjp";
    public static final String PASSWORD = "coucou";

    public static void main(String[] args) throws Exception {
        // use the keystore for private key/ certificates to be presented by this client,
        // if the server has setNeedClientCert(true); put the client certificate
        // in the server's truststore; the keystore on the client side must contain
        // the private key; the distinguished name of the client certificate does not matter
        System.setProperty("javax.net.ssl.keyStore", "keystore.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "jjjjjjjj");
        // first import server certificate (chain) into the truststore
        System.setProperty("javax.net.ssl.trustStore", "truststore.ks");
        System.setProperty("javax.net.ssl.trustStorePassword", "jjjjjjjj");
        URL url = new URL(DESTINATION);
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/plain");

        String encoding = new BASE64Encoder().encode((USERNAME + ":" + PASSWORD).getBytes());
        conn.setRequestProperty ("Authorization", "Basic " + encoding);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.connect();

        Certificate[] localCertificates = conn.getLocalCertificates();
        if (localCertificates != null){
            System.out.println("certificates sent to server:");
            for (int i = 0; i < localCertificates.length; i++) System.out.println(" " + localCertificates[i]);
        }

        OutputStream os = conn.getOutputStream();
        os.write("some data sent by the integration PC".getBytes());
        os.close();
        InputStream is = conn.getInputStream();
        while(is.read() != -1);
        is.close();
        conn.disconnect();

    }
}
