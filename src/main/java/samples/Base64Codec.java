package samples;

import java.util.Base64;

public class Base64Codec {
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	private static final char PAD = '=';
	
	public static String encode(byte[] b) {
		int i = 0;
		int l = b.length;
		StringBuffer sb = new StringBuffer(l * 2);
		while (i < l) {
			byte x = b[i];
			sb.append(ALPHABET.charAt(x >> 2 & 0x3F));
			i++;
			if (i < l) {
				byte y = b[i];
				sb.append(ALPHABET.charAt(((x & 0x3) << 4) + (y >> 4 & 0xF)));
				i++;
				if (i < l) {
					byte z = b[i];
					sb.append(ALPHABET.charAt((y << 2 & 0x3F) + (z >> 6 & 0x3)));
					sb.append(ALPHABET.charAt(z & 0x3F));
				} else {
					sb.append(ALPHABET.charAt(y << 2 & 0x3F));
					sb.append(PAD);
				}
			} else {
				sb.append(ALPHABET.charAt((x & 0x3) << 4));
				sb.append(PAD);
				sb.append(PAD);
			}
			i++;
			if (i % 57 == 0) sb.append("\n");
		}
		return(sb.toString());
	}
	
	public byte[] decode(String s) {
		return(null);
	}
	
	public static void main(String[] args) {
		System.out.println("coucou");
		System.out.println("size: " + ALPHABET.length());
		int m = 256;
		byte[] b = new byte[256];
		for (int i = 0; i < 256; i++) {
			b[i] = (byte)i;
		}
		System.out.println(encode(b));
		System.out.println(new String(Base64.getEncoder().encode(b)));
	}
}