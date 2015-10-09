package samples;

import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MultithreadSendFileToURL extends Thread {
	private static final int NB_THREADS = 10;
	private static String SRC_FILE = "2002-03-13T16-51_906312340a01011500305fb8f5f9f0b8_PCL01990000126-0a130637f70d065200029db9_0a130637f70d065200029db9.xml";
	private static String DST_URL = "http://inttest:5555/invoke/elemica.v1.v2_0_2.test/receiveLTM";
	private static String USERNAME = "jpc";
	private static String PASSWORD = "jjjjjjjj";
	
	private FileReader fr;
	private URL url;
	
	public MultithreadSendFileToURL(FileReader fr, URL url) {
		this.fr = fr;
		this.url = url;
	}
	
	public void run() {
		try {
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/xml");
			
			String encoding = new BASE64Encoder().encode((USERNAME + ":" + PASSWORD).getBytes());
			conn.setRequestProperty ("Authorization", "Basic " + encoding);
			
			conn.setDoOutput(true);
			conn.connect();
			System.out.println("raw response " + this + " ---------------------------------------");
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			char[] r = new char[4096];
			int read = fr.read(r);
			while (read != -1) {
				for (int i = 0; i < read; i++) {
					// System.out.print(r[expirationDate]);
				}
				out.write(r, 0, read);
				read = fr.read(r);
			}
			out.flush();
			out.close();
			System.out.println("raw request " + this + " --------------------------------------");
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			read = in.read(r);
			while (read != -1) {
				for (int i = 0; i < read; i++) {
					// System.out.print(r[expirationDate]);
				}
				read = in.read(r);
			}
			in.close();	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	throws Exception {
		FileReader fr = new FileReader(new File(SRC_FILE));
		URL url = new URL(DST_URL);
		MultithreadSendFileToURL[] t = new MultithreadSendFileToURL[NB_THREADS];
		for (int i = 0; i < NB_THREADS; i++) {
			t[i] = new MultithreadSendFileToURL(fr, url);
		}
		for (int i = 0; i < NB_THREADS; i++) {
			t[i].start();
		}
	}
	
}