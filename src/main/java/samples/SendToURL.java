package samples;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import sun.misc.BASE64Encoder;

import java.net.HttpURLConnection;
import java.net.URL;


public class SendToURL {
	
	private static String DST_URL = "http://intdev:5555/invoke/sampleLP.v1.url/queryCalloffData?tw!calloff_id=987654";
	private static String USERNAME = "jpc";
	private static String PASSWORD = "jjjjjjjj";
	
	public static void main(String[] args) throws Exception {
		SAXBuilder b = new SAXBuilder();
		XMLOutputter o = new XMLOutputter();
	    URL url = new URL(DST_URL);
	    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "text/xml");
		
		String encoding = new BASE64Encoder().encode((USERNAME + ":" + PASSWORD).getBytes());
	    conn.setRequestProperty ("Authorization", "Basic " + encoding);
		
	    conn.setDoOutput(true);
	    conn.connect();
		
		Document doc = b.build(conn.getInputStream());
		o.output(doc, System.out);
	}
}