package net.nio.protocol.http;

import net.nio.Dispatcher;
import net.nio.SecureWrapper;
import net.nio.Server;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import javax.net.ssl.*;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 9, 2005
 * Time: 4:49:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpsServer extends Server {
    private SSLContext sslContext;

    public HttpsServer(Dispatcher dispatcher, int port, SSLContext sslContext) throws IOException {
        super(dispatcher, ServerSocketChannel.open(), port);
        this.sslContext = sslContext;
    }

    public void pulse() {
    }

    public void socketConnected(Dispatcher dispatcher, SocketChannel socketChannel) throws IOException {
        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(false);
        SecureWrapper secureWrapper = new SecureWrapper(dispatcher, socketChannel, sslEngine, new HttpWorker(dispatcher, socketChannel));
        secureWrapper.open();
    }

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        X509V3CertificateGenerator generator = new X509V3CertificateGenerator();
        generator.setIssuerDN(new X509Name("CN=localhost, C=BE"));
        generator.setSubjectDN(new X509Name("CN=localhost, C=BE"));
        generator.setNotBefore(new Date(System.currentTimeMillis() - 3600000));
        generator.setNotAfter(new Date(System.currentTimeMillis() + 3600000));
        generator.setPublicKey(keyPair.getPublic());
        generator.setSerialNumber(new BigInteger(Long.toString(secureRandom.nextLong())));
        generator.setSignatureAlgorithm("MD5withRSA");
        X509Certificate certificate = generator.generateX509Certificate(keyPair.getPrivate());
        String password = "password";
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, password.toCharArray());
        keyStore.setKeyEntry("testkey", keyPair.getPrivate(), password.toCharArray(), new java.security.cert.Certificate[]{ certificate });
        keyStore.setCertificateEntry("testtrust", certificate);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password.toCharArray());
        KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(keyManagers, trustManagers, secureRandom);
        Dispatcher dispatcher = new Dispatcher(200);
        Thread thread = new Thread(dispatcher);
        thread.setName("dispatcher-" + thread.getId());
        thread.start();
        HttpsServer httpsServer = new HttpsServer(dispatcher, 8443, sslContext);
        httpsServer.open();
    }
}
