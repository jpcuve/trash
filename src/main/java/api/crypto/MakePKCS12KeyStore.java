package api.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Nov 2, 2005
 * Time: 2:12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class MakePKCS12KeyStore {
    public static final char[] PASSWORD = { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keyStore = KeyStore.getInstance("pkcs12", "BC");
        keyStore.load(null, PASSWORD);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        BufferedReader reader = new BufferedReader(new FileReader("resources/asusserver2.pkcs8"));
        String line = reader.readLine();
        if (line.indexOf("-----BEGIN") != 0) throw new UnsupportedEncodingException("file is not base64 encoded");
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null && line.indexOf("-----END") != 0) sb.append(line);
        byte[] keyData = Base64.getDecoder().decode(sb.toString());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyData);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey alicePrivateKey = keyFactory.generatePrivate(keySpec);
        X509Certificate aliceCertificate = (X509Certificate)certificateFactory.generateCertificate(new FileInputStream("resources/asusserver2.der"));
        keyStore.setKeyEntry("asusserver2", alicePrivateKey, PASSWORD, new Certificate[]{ aliceCertificate });
        keyStore.store(new FileOutputStream("resources/asusserver2.pkcs12"), PASSWORD);
    }
}
