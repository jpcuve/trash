/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 11, 2002
 * Time: 12:35:08 PM
 * To change this template use Options | File Templates.
 */
package net.symmetricencryption;

import java.io.UnsupportedEncodingException;

public class MiniFrog {
    public static final String ENCODING = "UTF-8";

    public static int[] r1 = new int[4];
    public static int[] r2 = new int[4];

    public static int next(){
        r1[0] = (int)((1403580 * (long)r1[2] - 810728 * (long)r1[3]) % (Integer.MAX_VALUE - 209));
        r2[0] = (int)((527612 * (long)r2[1] - 1370589 * (long)r2[3]) % (Integer.MAX_VALUE - 22853));
        for (int i = 3; i > 0; i--){
            r1[i] = r1[i - 1];
            r2[i] = r2[i - 1];
        }
        return (r1[0] - r2[0]) % (Integer.MAX_VALUE - 209);
    }

    public static String write(byte[] b){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) sb.append(" " + b[i]);
        return sb.toString();
    }

    private static int[] bomb(byte[] key, int size){
        int[] bomb = new int[size];
        for (int i = 0; i < size; i++){
            bomb[i] = (i + 1) % size;
        }
        int[] r1 = new int[4];
        int[] r2 = new int[4];
        for (int i = 1; i < 4; i++){
            r1[i] = key[(i + key[(i + 1) % key.length]) % key.length];
            r2[i] = key[(r1[i] + key[(r1[i] + 1) % key.length]) % key.length];
        }
        for (int i = 0; i < size; i++){
            r1[0] = (int)((1403580 * (long)r1[2] - 810728 * (long)r1[3]) % (Integer.MAX_VALUE - 209));
            r2[0] = (int)((527612 * (long)r2[1] - 1370589 * (long)r2[3]) % (Integer.MAX_VALUE - 22853));
            for (int j = 3; j > 0; j--){
                r1[j] = r1[j - 1];
                r2[j] = r2[j - 1];
            }
            int j = Math.abs((r1[0] - r2[0]) % (Integer.MAX_VALUE - 209)) % size;
            if (bomb[i] != j && bomb[j] != i){
                int z = bomb[i];
                bomb[i] = bomb[j];
                bomb[j] = z;
            }
        }
        return bomb;
    }

    private static byte[] encode(byte[] mes, byte[] key){
        byte[] b = new byte[mes.length];
        int[] bomb = bomb(key, mes.length);
        for (int i = 0; i < mes.length; i++){
            b[i] = mes[i];
        }
        for (int i = 0; i < mes.length; i++){
            b[i] ^= key[i % key.length];
            int j = (i + 1) % mes.length;
            b[j] ^= b[i];
            b[bomb[i]] ^= b[i];
        }
        return b;
    }

    private static byte[] decode(byte[] mes, byte[] key){
        byte[] b = new byte[mes.length];
        int[] bomb = bomb(key, mes.length);
        for (int i = 0; i < mes.length; i++){
            b[i] = mes[i];
        }
        for (int i = mes.length - 1; i >= 0; i--){
            b[bomb[i]] ^= b[i];
            int j = (i + 1) % mes.length;
            b[j] ^= b[i];
            b[i] ^= key[i % key.length];
        }
        return b;
    }

    private static final String HEX = "0123456789ABCDEF";

    private static byte[] toByteArray(String s) throws UnsupportedEncodingException{
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++){
            int j = HEX.indexOf(s.charAt(i * 2)) * 16 + HEX.indexOf(s.charAt(i * 2 + 1));
            b[i] = (byte)j;
        }
        return b;
    }

    private static String toString(byte[] b) throws UnsupportedEncodingException{
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX.charAt((b[i] & 0xF0) >> 4));
            sb.append(HEX.charAt((b[i] & 0xF)));
        }
        return sb.toString();
    }

    public static String encrypt(String message, String key) throws UnsupportedEncodingException{
        return toString(encode(message.getBytes(ENCODING), key.getBytes(ENCODING)));
    }

    public static String decrypt(String message, String key) throws UnsupportedEncodingException{
        return new String(decode(toByteArray(message), key.getBytes(ENCODING)), ENCODING);
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++){
            String mes = "Your reference " + i;
            String key = "Our key";
            String enc = encrypt(mes, key);
            System.out.println("Mes=" + mes + ", Enc=" + enc + ", Dec=" + decrypt(enc, key));
        }
    }
}
