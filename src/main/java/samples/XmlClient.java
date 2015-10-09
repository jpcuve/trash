package samples;

public class XmlClient {
	private static String URN_TW = "urn:transwide-com:v1";
	private static String URN_SOAP = "urn:schemas-xmlsoap-org:soap.v1";
	private static String METHOD = "reverse";
	
	public static void main(String[] args) throws Exception {
/*
		Namespace soap = Namespace.getNamespace("soap", URN_SOAP);
		Namespace tw = Namespace.getNamespace("tw", URN_TW);
		Element root = new Element("Envelope", soap);
		Element body = new Element("Body", soap);
		Element method = new Element(METHOD, tw);
		Element str = new Element("str", tw);
		root.addContent(body);
		body.addContent(method);
		method.addContent(str);
		str.addContent("A tést");
		XMLOutputter o = new XMLOutputter(" ", true);
	    URL url = new URL("http://localhost/intranet/rpc");
	    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "text/xml");
		conn.setRequestProperty("SOAPMethodName", URN_TW + "#" + METHOD);
	    conn.setDoOutput(true);
	    conn.connect();
	    OutputStream out = conn.getOutputStream();
		o.output(new Document(root), out);
	    out.close();
	    InputStream in = conn.getInputStream();
	    byte[] rgb = new byte[1024];
	    int cb = in.read(rgb);
	    in.close();
	    System.out.println(new String(rgb, 0, cb));
*/
	}
}