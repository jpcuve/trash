package samples;

import org.jdom.*;
import org.jdom.output.*;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;

public class XmlMailSender {
	private static final String SMTP = "intdev";
	private static final String SENDER = "jp@transwide.com";
	private static final String RECIPIENT = "jp@transwide.com";
	private static final String ENCODING = "UTF-8";

	public static void main(String[] args) throws Exception {
		System.getProperties().put("mail.smtp.host", SMTP);
		Session se = javax.mail.Session.getDefaultInstance(System.getProperties(), null);
		Transport tr = se.getTransport("smtp");
		InternetAddress[] ia = new InternetAddress[1];
		ia[0] = new InternetAddress(RECIPIENT); 
		MimeMessage mm = new MimeMessage(se);
		mm.setFrom(new InternetAddress(SENDER));
		MimeMultipart mp = new MimeMultipart();
		mp.setSubType("related");
		
		// part 1: html
		MimeBodyPart mbp1 = new MimeBodyPart();
		String html = "<html><body><b>Message in you inbox</b></body></html>";
		mbp1.setContent(html, "text/html");
		mp.addBodyPart(mbp1);
		
		// part 2: xml
		MimeBodyPart mbp2 = new MimeBodyPart();
		Element e = new Element("test");
		e.addContent("value");
		Document d = new Document(e);
		mbp2.setDataHandler(new DataHandler(new XmlDataSource(d, ENCODING, "myxml")));
		// mbp2.setContent(s.toString(ENCODING), "text/plain");
		mp.addBodyPart(mbp2);
		
		mm.setContent(mp);
		mm.setSubject("xml");
		tr.send(mm, ia);
	}
}