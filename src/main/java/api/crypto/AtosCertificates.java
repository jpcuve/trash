package api.crypto;

import java.io.FileInputStream;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Aug 17, 2005
 * Time: 9:33:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class AtosCertificates {
    public static void main(String[] args) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate ca = (X509Certificate)certificateFactory.generateCertificate(new FileInputStream("ca.crt"));
        X509Certificate twa1 = (X509Certificate)certificateFactory.generateCertificate(new FileInputStream("twa1.crt"));
        X509Certificate twa2 = (X509Certificate)certificateFactory.generateCertificate(new FileInputStream("twa2.crt"));
        System.out.println("alg: " + ca.getSigAlgName());
        Signature signature = Signature.getInstance(ca.getSigAlgName());
        signature.initVerify(ca);
        signature.update(ca.getTBSCertificate());
        System.out.println("verification ca: " + signature.verify(ca.getSignature()));
        signature = Signature.getInstance(twa1.getSigAlgName());
        signature.initVerify(ca);
        signature.update(twa1.getTBSCertificate());
        System.out.println("verification twa1: " + signature.verify(twa1.getSignature()));
        signature = Signature.getInstance(twa2.getSigAlgName());
        signature.initVerify(ca);
        signature.update(twa2.getTBSCertificate());
        System.out.println("verification twa2: " + signature.verify(twa2.getSignature()));
    }
}
