package samples;

import org.jdom.*;
import org.jdom.output.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;

public class XmlDataSource implements DataSource {
	byte[] bytes;
	String name;
	
	public XmlDataSource(Document doc, String encoding, String name)
	throws UnsupportedEncodingException, IOException {
		this.name = name;
		bytes = new XMLOutputter().outputString(doc).getBytes(encoding);
	}
	
	public String getContentType() {
		return("text/xml");
	}
	
	public InputStream getInputStream() {
		return(new ByteArrayInputStream(bytes, 0, bytes.length - 2)); // remove final CR/LF
	}
	
	public String getName() {
		return(name);
	}
	
	public OutputStream getOutputStream()
	throws IOException {
		throw new FileNotFoundException();
	}
	
	/**
	* Demo
	* The XML is attached with both types text/plain and text/xml
	* text/xml requires usage of an XmlDataSource
	* attention: TransWide currently uses text/plain part only
	*/

	private static final String SMTP = "127.0.0.1";
	private static final String SENDER = "jpc@localhost";
	private static final String RECIPIENT = "jpc@localhost";
	private static final String ENCODING = "UTF-8";
	
	public static void main(String[] args)
	throws Exception {
		System.getProperties().put("mail.smtp.host", SMTP);
		Session se = Session.getDefaultInstance(System.getProperties(), null);
		Transport tr = se.getTransport("smtp");
		InternetAddress[] ia = new InternetAddress[1];
		ia[0] = new InternetAddress(RECIPIENT); 
		MimeMessage mm = new MimeMessage(se);
		mm.setFrom(new InternetAddress(SENDER));
		MimeMultipart mp = new MimeMultipart();
		mp.setSubType("related");
		
		// build XML document
		Element e = new Element("test_tag");
		e.addContent("value with àccénts");
		Document doc = new Document(e);
		
		// part 1: text/plain
		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setContent(new String(new XMLOutputter().outputString(doc).getBytes(ENCODING)), "text/plain");
		mp.addBodyPart(mbp1);
		
		// part 2: text/xml
		MimeBodyPart mbp2 = new MimeBodyPart();
		mbp2.setDataHandler(new DataHandler(new XmlDataSource(doc, ENCODING, "myxml")));
		mp.addBodyPart(mbp2);
		
		mm.setContent(mp);
		mm.setSubject("xml");
		tr.send(mm, ia);
	}
}