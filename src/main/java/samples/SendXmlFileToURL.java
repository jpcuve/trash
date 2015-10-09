package samples;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class SendXmlFileToURL {
	
	private static String SRC_FILE = "GCATRANS/SampleCalloffQueryNewUpdated.xml";
	private static String DST_URL = "http://intdev:5555/invoke/gcatrans.v1.xml/queryNewCalloffs";
	// private static String DST_URL = "http://comm.transwide.com:10200/invoke/elemica.v1.v2_0_2.test/receiveLTM";
	private static String USERNAME = "jpc";
	private static String PASSWORD = "jjjjjjjj";
	
	public static void main(String[] args) throws Exception {
		/* this is for https connections
		System.setProperty("java.protocol.handler.pkgs",
        "com.sun.net.ssl.internal.www.protocol");
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		*/
		Document doc = null;
		SAXBuilder b = new SAXBuilder();
		File f = new File(SRC_FILE);
		doc = b.build(f);
		XMLOutputter o = new XMLOutputter();
	    URL url = new URL(DST_URL);
	    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "text/xml");
		
		String encoding = new BASE64Encoder().encode((USERNAME + ":" + PASSWORD).getBytes());
	    conn.setRequestProperty ("Authorization", "Basic " + encoding);
		
	    conn.setDoOutput(true);
	    conn.connect();
	    OutputStream out = conn.getOutputStream();
		o.output(doc, out);
	    out.close();
		
		doc = b.build(conn.getInputStream());
		o.output(doc, System.out);
	}
}