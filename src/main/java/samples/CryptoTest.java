package samples;

import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.security.*;
import java.security.cert.X509Certificate;

public class CryptoTest {
	public static void main(String[] args)
	throws Exception {
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream("../.keystore"), null);
		X509Certificate x509 = (X509Certificate)ks.getCertificate("jpc");
		PublicKey puk = x509.getPublicKey();
		PrivateKey prk = (PrivateKey)ks.getKey("jpc", "jjjjjjjj".toCharArray());
		MessageDigest md = MessageDigest.getInstance("SHA1");
		byte[] digest = md.digest("This is the message".getBytes());
		System.out.println("Digest=" + new BASE64Encoder().encode(digest));
		Signature s = Signature.getInstance("DSA");
		s.initSign(prk);
		s.update(digest);
		byte[] sign = s.sign();
		// verification
		s.initVerify(puk);
		s.update(digest);
		boolean ok = s.verify(sign);
		System.out.println(ok ? "Message ok!" : "Message fake");
		// take response tampered message
		digest = md.digest("Fake message".getBytes());
		System.out.println("Digest=" + new BASE64Encoder().encode(digest));
		s.initVerify(puk);
		s.update(digest);
		ok = s.verify(sign);
		System.out.println(ok ? "Message ok!" : "Message fake");		
	}
}