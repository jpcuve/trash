import java.math.BigInteger;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.CRC32;

/**
 * Created by jpc on 16/12/14.
 */
public class DevelopmentKey {
    public static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String POW = "9A237383441239118654689163138999112A92";
    public static final String MOD = "97G82799DEE3723DB228E2G65CEBF13A";
    public static final String SEED = "71577287";
    public static final Random RANDOM = new Random();

    public DevelopmentKey() {
    }

    public static BigInteger getValue(String s, int base){
        final StringBuilder sb = new StringBuilder();
        for (char c: s.toCharArray()){
            sb.append(CHARACTERS.charAt(CHARACTERS.indexOf(c) - 1));
        }
        return new BigInteger(sb.toString(), base);
    }

    public static short getCRC(String s, int i, byte[] bytes) {
        final CRC32 crc32 = new CRC32();
        int k;
        if(s != null) {
            for(k = 0; k < s.length(); ++k) {
                char byte0 = s.charAt(k);
                crc32.update(byte0);
            }
        }

        crc32.update(i);
        crc32.update(i >> 8);
        crc32.update(i >> 16);
        crc32.update(i >> 24);

        for(k = 0; k < bytes.length - 2; ++k) {
            byte var6 = bytes[k];
            crc32.update(var6);
        }

        return (short)((int)crc32.getValue());
    }


    public static String encodeGroups(BigInteger val){
        final BigInteger seed = getValue(SEED, 10);
        final StringBuilder sb = new StringBuilder();
        boolean sep = false;
        while(val.signum() != 0){
            if (sep) sb.append('-');
            sb.append(encodeGroup(val.mod(seed).intValue()));
            sep = true;
            val = val.divide(seed);
        }
        return sb.toString();
    }

    public static String encodeGroup(int n){
        final StringBuilder sb = new StringBuilder();
        int len = CHARACTERS.length();
        for (int i = 0; i < 5; i++){
            sb.append(CHARACTERS.charAt(n % len));
            n /= len;
        }
        return sb.toString();
    }

    public static String makeKey(String name, int days, byte version) {
        int randomId = RANDOM.nextInt(100000);
        byte[] bkey = new byte[12];
        bkey[0] = 1;
        bkey[1] = version;
        Date d = new Date();
        long ld = d.getTime() >> 16;
        bkey[2] = (byte)((int)(ld & 255L));
        bkey[3] = (byte)((int)(ld >> 8 & 255L));
        bkey[4] = (byte)((int)(ld >> 16 & 255L));
        bkey[5] = (byte)((int)(ld >> 24 & 255L));
        days &= '\uffff';
        bkey[6] = (byte)(days & 255);
        bkey[7] = (byte)(days >> 8 & 255);
        bkey[8] = 105;
        bkey[9] = -59;
        bkey[10] = 0;
        bkey[11] = 0;
        short w = getCRC(name, randomId, bkey);
        bkey[10] = (byte)(w & 255);
        bkey[11] = (byte)(w >> 8 & 255);
        BigInteger k0 = new BigInteger(bkey);
        BigInteger k1 = k0.modPow(getValue(POW, 10), getValue(MOD, 16));
        return String.format("%05d-%s", randomId, encodeGroups(k1));
    }

    public static void main(String[] args) {
        System.out.printf("Input user or company name: ");
        final Scanner scanner = new Scanner(System.in);
        final String name = scanner.next();
        System.out.printf("%s%n", makeKey(name, 0, (byte) 14));
    }
}
