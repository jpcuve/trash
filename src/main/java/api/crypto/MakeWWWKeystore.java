package api.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Aug 23, 2005
 * Time: 11:30:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class MakeWWWKeystore {
    private static final char[] PASS = new char[]{ 't', 'o', 't', 'a', 'l', 'w', 'i', 'd', 'e'};
    public static void main(String[] args) throws Exception {
        /*
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate ca = (X509Certificate)certificateFactory.generateCertificate(new FileInputStream("resources/GteCyberTrustGlobalRoot.crt"));
        X509Certificate int1 = (X509Certificate)certificateFactory.generateCertificate(new FileInputStream("resources/ComodoSecurityServicesCA.crt"));
        X509Certificate www = (X509Certificate)certificateFactory.generateCertificate(new FileInputStream("resources/www_transwide_com.crt"));
        File file = new File("resources/www.2005.unencrypted.der");
        FileInputStream fis = new FileInputStream(file);
        byte[] keyData = new byte[(int)file.length()];
        int read = fis.read(keyData);
        fis.close();
        System.out.println("read " + read + " bytes");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyData);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        System.out.println(" algorithm=" + privateKey.getAlgorithm());
        System.out.println(" format=" + privateKey.getFormat());
        */
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keyStoreFrom = KeyStore.getInstance("pkcs12", "BC");
        KeyStore keyStoreTo = KeyStore.getInstance("jks");
        keyStoreFrom.load(new FileInputStream("resources/www.2005.pkcs12"), PASS);
        keyStoreTo.load(null, PASS);
        for (Enumeration<String> e = keyStoreFrom.aliases(); e.hasMoreElements();){
            String alias = e.nextElement();
            System.out.println("alias=" + alias + ", key entry? " + keyStoreFrom.isKeyEntry(alias));
            if (keyStoreFrom.isKeyEntry(alias)){
                Key key = keyStoreFrom.getKey(alias, PASS);
                Certificate[] chain = keyStoreFrom.getCertificateChain(alias);
                keyStoreTo.setKeyEntry(alias, key, PASS, chain);
            }
        }
        keyStoreTo.store(new FileOutputStream("resources/www.2005.jks"), PASS);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStoreTo, PASS);
        KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStoreTo);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(keyManagers, trustManagers, secureRandom);
        SSLServerSocket sslServerSocket = (SSLServerSocket)sslContext.getServerSocketFactory().createServerSocket(4443);
        SSLSocket sslSocket = (SSLSocket)sslServerSocket.accept();
        class SSLSocketProcessor extends Thread{
            private SSLSocket sslSocket;

            public SSLSocketProcessor(SSLSocket sslSocket) {
                this.sslSocket = sslSocket;
            }

            public void run() {
                int b;
                try{
                    while((b = sslSocket.getInputStream().read()) != -1) System.out.print((char)b);
                } catch(IOException x){
                    x.printStackTrace();
                }
            }
        }
        new SSLSocketProcessor(sslSocket).start();
    }
}
