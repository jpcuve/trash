package samples;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class SendFileToURL {
	
	private static String SRC_FILE = "GCATRANS/SampleCalloffQueryNewUpdated.xml";
	private static String DST_URL = "http://intlinux:5555/invoke/sample/pronto?";
	// private static String DST_URL = "http://intdev:5555/invoke/gcatrans.v1.xml/queryNewCalloffs";
	private static String USERNAME = "jpc";
	private static String PASSWORD = "jjjjjjjj";
	
	public static void main(String[] args) throws Exception {
		/* this is for https connections
		System.setProperty("java.protocol.handler.pkgs",
        "com.sun.net.ssl.internal.www.protocol");
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		*/
		int nbtrials=1;
		for (int trials=0; trials < nbtrials ; trials++) {
		
		
			FileReader fr = new FileReader(new File(SRC_FILE));
		    URL url = new URL(DST_URL);
		    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-Type", "text/xml");
			
			String encoding = new String(Base64.getEncoder().encode((USERNAME + ":" + PASSWORD).getBytes()));
		    conn.setRequestProperty ("Authorization", "Basic " + encoding);
			
		    conn.setDoOutput(true);
		    conn.connect();
			System.out.println("raw response " + trials + " ---------------------------------------");
		    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			char[] r = new char[4096];
			int read = fr.read(r);
			while (read != -1) {
				for (int i = 0; i < read; i++) {
					System.out.print(r[i]);
				}
				out.write(r, 0, read);
				read = fr.read(r);
			}
			out.flush();
		    out.close();
			System.out.println("raw request " + trials + " --------------------------------------");
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			read = in.read(r);
			while (read != -1) {
				for (int i = 0; i < read; i++) {
					System.out.print(r[i]);
				}
				read = in.read(r);
			}
			in.close();
		}
	}
}