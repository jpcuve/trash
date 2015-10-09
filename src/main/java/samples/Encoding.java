package samples;

public class Encoding {
	
	public static void main(String[] args)
	throws Exception {
		String s = "Réservons 37é2";
		System.out.println(s);
		byte[] b = s.getBytes("UTF-8");
		String sutf8 = new String(b, "ISO-8859-1");
		System.out.println(sutf8);
		byte[] b2 = sutf8.getBytes("UTF-8");
		String sutf82 = new String(b2, "ISO-8859-1");
		System.out.println(sutf82);
	}
}