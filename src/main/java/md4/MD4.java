package md4;

import java.nio.charset.Charset;

/**
 * @author jpc
 */
public class MD4 {
    private static final long[] INITIAL = new long[]{ 0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476 };
    private static final int[] FROT = new int[]{ 3, 7, 11, 19 };
    private static final int[] GROT = new int[]{ 3, 5, 9, 13 };
    private static final int[] GPOS = new int[]{ 0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15 };
    private static final int[] HROT = new int[]{ 3, 9, 11, 15 };
    private static final int[] HPOS = new int[]{ 0, 8, 4, 12, 2, 10, 6, 14, 1, 9, 5, 13, 3, 11, 7, 15 };
    private static final String DIGITS = "0123456789ABCDEF";

    private static long f(long x, long y, long z){
        return (x & y) | (~x & z);
    }

    private static long g(long x, long y, long z){
        return (x & y) | (x & z) | (y & z);
    }

    private static long h(long x, long y, long z){
        return x ^ y ^ z;
    }

    private static long rot(long x, int s){
        x &= 0xFFFFFFFF;
        return (x << s) | (x >> (32 - s));
    }

    private static long rev(long x){
        x &= 0xFFFFFFFF;
        return ((x & 0xFF00FF00) >> 8) | ((x & 0x00FF00FF) << 8);
    }

    public static String toHexString(final byte[] bs, final char separator){
        final StringBuilder sb = new StringBuilder();
        for (byte b: bs){
            sb.append(DIGITS.charAt((b & 0xF0) >> 4)).append(DIGITS.charAt(b & 0xF));
            if (separator != 0) sb.append(separator);
        }
        return sb.toString();
    }

    public static String toHexString(final long[] ls, final char separator){
        final StringBuilder sb = new StringBuilder();
        for (long l: ls){
            for (int i = 0; i < 16; i++) sb.append(DIGITS.charAt((int)((l >> ((15 - i) * 4))) & 0xF));
            if (separator != 0) sb.append(separator);
        }
        return sb.toString();
    }

    public static String digest(final String s){
        final byte[] raw = s.getBytes(Charset.forName("UTF-16"));
        final int total = ((raw.length >> 9) + 1) << 9;
        final long[] data = new long[total >> 3];
        int j;
        for (j = 0; j < raw.length; j++){
            final int i = j >> 3;
            data[i] = (data[i] << 8) | ((long)raw[j] & 0xFF);
        }
        data[j >> 3] |= (0x80 << ((j % 8) << 3));
        data[data.length - 1] = data.length;

        final long[] o = new long[INITIAL.length];
        final long[] oo = new long[INITIAL.length];
        for (int i = 0; i < INITIAL.length; i++) o[i] = INITIAL[i];

        for (int k = 0; k < (data.length >> 3); k++){ // process 8 * 64bits at getLicenseTypeString time
            final long[] x = new long[16];
            for (int l = 0; l < x.length / 2; l++){
                final long d = data[(k >> 3) + l];
                x[l * 2] = (d >> 32);
                x[l * 2 + 1] = d;
                x[l * 2] &= 0xFFFFFFFF;
                x[l * 2 + 1] &= 0xFFFFFFFF;
            }
            for (int i = 0; i < o.length; i++) oo[i] = o[i];
            for (int i = 0; i < 16; i++){ // round 1
                int a = i % 4;
                int b = (i + 1) % 4;
                int c = (i + 2) % 4;
                int d = (i + 3) % 4;
                o[a] = rot(o[a] + f(o[b], o[c], o[d]) + x[i], FROT[a]);
            }
            for (int i = 0; i < 16; i++){ // round 2
                int a = i % 4;
                int b = (i + 1) % 4;
                int c = (i + 2) % 4;
                int d = (i + 3) % 4;
                o[a] = rot(o[a] + g(o[b], o[c], o[d]) + x[GPOS[i]] + 0x5A827999, GROT[a]);
            }
            for (int i = 0; i < 16; i++){ // round 3
                int a = i % 4;
                int b = (i + 1) % 4;
                int c = (i + 2) % 4;
                int d = (i + 3) % 4;
                o[a] = rot(o[a] + h(o[b], o[c], o[d]) + x[HPOS[i]] + 0x6ED9EBA1, HROT[a]);
            }
            for (int i = 0; i < o.length; i++) o[i] += oo[i];
        }
        return toHexString(o, (char)0);
    }

    public static void main(String[] args) {
        final String[] ss = {
                "",
                "this is a test",
                "this is b test"
        };
        for (final String s: ss){
            final byte[] raw = s.getBytes(Charset.forName("UTF-16"));
            final int total = ((raw.length >> 9) + 1) << 9;
            final long[] data = new long[total >> 3];
            int j;
            for (j = 0; j < raw.length; j++){
                final int i = j >> 3;
                data[i] = (data[i] << 8) | ((long)raw[j] & 0xFF);
            }
            data[j >> 3] |= (0x80 << ((j % 8) << 3));
            data[data.length - 1] = data.length;

            final long[] o = new long[INITIAL.length];
            final long[] oo = new long[INITIAL.length];
            for (int i = 0; i < INITIAL.length; i++) o[i] = INITIAL[i];

            for (int k = 0; k < (data.length >> 3); k++){ // process 8 * 64bits at getLicenseTypeString time
                final long[] x = new long[16];
                for (int l = 0; l < x.length / 2; l++){
                    final long d = data[(k >> 3) + l];
                    x[l * 2] = (d >> 32);
                    x[l * 2 + 1] = d;
                    x[l * 2] &= 0xFFFFFFFF;
                    x[l * 2 + 1] &= 0xFFFFFFFF;
                }
                for (int i = 0; i < o.length; i++) oo[i] = o[i];
                for (int i = 0; i < 16; i++){ // round 1
                    int a = i % 4;
                    int b = (i + 1) % 4;
                    int c = (i + 2) % 4;
                    int d = (i + 3) % 4;
                    o[a] = rot(o[a] + f(o[b], o[c], o[d]) + x[i], FROT[a]);
                }
                for (int i = 0; i < 16; i++){ // round 2
                    int a = i % 4;
                    int b = (i + 1) % 4;
                    int c = (i + 2) % 4;
                    int d = (i + 3) % 4;
                    o[a] = rot(o[a] + g(o[b], o[c], o[d]) + x[GPOS[i]] + 0x5A827999, GROT[a]);
                }
                for (int i = 0; i < 16; i++){ // round 3
                    int a = i % 4;
                    int b = (i + 1) % 4;
                    int c = (i + 2) % 4;
                    int d = (i + 3) % 4;
                    o[a] = rot(o[a] + h(o[b], o[c], o[d]) + x[HPOS[i]] + 0x6ED9EBA1, HROT[a]);
                }
                for (int i = 0; i < o.length; i++) o[i] += oo[i];
            }
            System.out.printf("%s: %s%n", s, toHexString(o, (char)0));
        }
    }

}
