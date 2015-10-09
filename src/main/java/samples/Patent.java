package samples;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Sep 4, 2008
 * Time: 2:13:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Patent {
    private static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGIT = "0123456789";

    public static String cleanApplication(final String s){
        if (s == null) return "";
        int alphaCount = 0;
        int digitCount = 0;
        boolean done = false;
        final StringBuffer sb = new StringBuffer();
        for (final char c: s.toUpperCase().toCharArray()) if (!done) {
            if (ALPHA.indexOf(c) != -1) if (digitCount == 0){
                sb.append(c);
                alphaCount++;
            } else{
                done = true;
            }
            if (DIGIT.indexOf(c) != -1) if (alphaCount == 0){
                done = true;
            } else{
                sb.append(c);
                digitCount++;
            }
        }
        if (alphaCount != 2 || digitCount < 4) return s.toUpperCase();
        final int thisYear = 1900 + new Date().getYear();
        int thisCentury = thisYear / 100;
        int century = Integer.parseInt(sb.toString().substring(2, 4));
        if (century != thisCentury && century != thisCentury - 1){
            sb.insert(2, Integer.toString(thisCentury - (century < thisYear % 100 ? 0 : 1)));
            digitCount += 2;
        }
        for (int i = 0; i < 11 - digitCount; i++) sb.insert(6, '0');
        return sb.toString();
    }

    public static String cleanPublication(final String s){
        if (s == null) return "";
        int alphaCount = 0;
        int digitCount = 0;
        boolean done = false;
        final StringBuffer sb = new StringBuffer();
        for (final char c: s.toUpperCase().toCharArray()) if (!done) {
            if (ALPHA.indexOf(c) != -1) if (digitCount == 0){
                sb.append(c);
                alphaCount++;
            } else{
                done = true;
            }
            if (DIGIT.indexOf(c) != -1) if (alphaCount == 0){
                done = true;
            } else{
                sb.append(c);
                digitCount++;
            }
        }
        if (alphaCount != 2 || digitCount == 0) return s.toUpperCase();
        for (int i = 0; i < 7 - digitCount; i++) sb.insert(2, '0');
        return sb.toString();
    }

    private final static String[] TESTS = new String[]{ "", "123ABC", "ABC123", "EP1234567A1", "EP0734567A1", "EP123", "ep123a1" };

    public static void main(String[] args) {
        for (final String test: TESTS) System.out.println(test + " -> " + cleanApplication(test));

    }
}
