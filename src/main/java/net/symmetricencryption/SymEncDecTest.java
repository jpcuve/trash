/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 10, 2002
 * Time: 1:11:57 PM
 * To change this template use Options | File Templates.
 */
package net.symmetricencryption;

import java.io.UnsupportedEncodingException;

public class SymEncDecTest {

    public static String encrypt(String theText) {
        String output = "";
        int[] Temp = new int[theText.length()];
        int[] Temp2 = new int [theText.length()];
        int rnd = -1;
        int nSize = theText.length();
        for (int i = 0; i < nSize; i++) {
            rnd = (int)Math.round(Math.random() * 122) + 68;
            Character c = new Character(theText.charAt(i));
            Temp[i] = c.charValue() + rnd;
            Temp2[i] = rnd;
        }
        for (int i = 0; i < nSize; i++) {
            char c = (char)Temp[i];
            char d = (char)Temp2[i];
            output += new Character(c);
            output += new Character(d);
        }
        return output;
    }

    public static String decrypt(String theText) {
        String output = "";
        int[] Temp = new int[theText.length()];
        int[] Temp2 = new int [theText.length()];
        int nSize = theText.length();
        for (int i = 0; i < nSize; i+=2) {
            Character c = new Character(theText.charAt(i));
            Character d = new Character(theText.charAt(i+1));
            Temp[i] = c.charValue();
            Temp2[i] = d.charValue();
        }
        for (int i = 0; i < nSize; i+=2) {
            char c = (char)(Temp[i] - Temp2[i]);
            output += c;
        }
        return output;
    }

    public static String encode(String mes, String key) throws UnsupportedEncodingException {
        String hex = "0123456789ABCDEF";
        byte[] m = mes.getBytes("UTF-8");
        byte[] k = key.getBytes("UTF-8");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < m.length; i++) {
            int j = m[i] ^ k[i % k.length];
            sb.append(hex.charAt((j & 0xF0) >> 4));
            sb.append(hex.charAt((j & 0xF)));
        }
        return sb.toString();
    }

    public static String decode(String mes, String key) throws UnsupportedEncodingException {
        String hex = "0123456789ABCDEF";
        byte[] m = new byte[mes.length() / 2];
        byte[] k = key.getBytes("UTF-8");
        for (int i = 0; i < m.length; i++){
            int j = hex.indexOf(mes.charAt(i * 2)) * 16 + hex.indexOf(mes.charAt(i * 2 + 1));
            m[i] = (byte)(j^ k[i % k.length]);
        }
        return new String(m, "UTF-8");
    }

    public static void main(String[] args) throws Exception {
        final String key = "TO_BE_CHANGED";
        final String reference = "some reference from DSM";
        String s = SymEncDecTest.encode(reference, key);
        System.out.println("Reference=" + reference);
        System.out.println("After encoding=" + s);
        System.out.println("After decoding=" + decode(s, key));
        System.out.println("After encryption=" + encrypt(reference));
        System.out.println("After decryption=" + decrypt(encrypt(reference)));
    }
}
