package samples;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;

/**
 * Created by jpc on 24-01-17.
 */
public class MailEml {
    public static void main(String[] args) throws Exception {
        final Session session = Session.getDefaultInstance(System.getProperties(), null);
        final MimeMessage message = new MimeMessage(session);
        message.setSubject("Some example eml file");
        message.setFrom(new InternetAddress("jpcuvelliez@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{
                new InternetAddress("jpcuvelliez@gmail.com"),
                new InternetAddress("jean-pierre.cuvelliez@skynet.be")
        });
        MimeMultipart mp = new MimeMultipart();
        mp.setSubType("related");

        // part 1: html
        final MimeBodyPart part1 = new MimeBodyPart();
        mp.addBodyPart(part1);
        String html = "<html><body><b>Message in your inbox</b></body></html>";
        part1.setContent(html, "text/html");

        // part 2: xml
        MimeBodyPart part2 = new MimeBodyPart();
        mp.addBodyPart(part2);
        part2.setDataHandler(new DataHandler(new DataSource() {
            final byte[] data = new byte[]{ 0x01, 0x02, 0x03 };
            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(data);
            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                throw new IOException();
            }

            @Override
            public String getContentType() {
                return "application/octet-stream";
            }

            @Override
            public String getName() {
                return null;
            }
        }));

        message.setContent(mp);
        final File f = new File(new File(System.getProperty("user.dir", ".")), "sample.eml");
        System.out.println(f.getAbsolutePath());
        final FileOutputStream fos = new FileOutputStream(f);
        message.writeTo(fos);
        fos.close();

    }
}
