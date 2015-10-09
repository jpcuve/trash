package api.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Feb 25, 2005
 * Time: 3:18:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Providers {
    private static final String MESSAGE = "this is a new message that is going to be encrypted than decrypted";

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        for (Provider provider: Security.getProviders()){
            System.out.println("provider: " + provider.getName());
            for (Provider.Service service: provider.getServices()){
                System.out.println(" [" + provider + "] " + service.getType() + " (" + service.getAlgorithm() + ")");
            }
        }

        // simple example of signing

        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(1024, secureRandom);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        System.out.println("private key: " + keyPair.getPrivate());
        System.out.println("public key: " + keyPair.getPublic());

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-384", "BC");
        byte[] digest = messageDigest.digest(MESSAGE.getBytes());
        System.out.println("message digest: " + messageDigest);

        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
        byte[] signature = cipher.doFinal(digest);

        System.out.println("signature size: " + signature.length);

        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
        byte[] digest2 = cipher.doFinal(signature);
        System.out.println("signature verified: " + Arrays.equals(digest, digest2));

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "BC");


    }
}
