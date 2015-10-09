/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 23, 2003
 * Time: 5:45:33 PM
 * To change this template use Options | File Templates.
 */
package net.symmetricencryption;

public class PseudoRandom {
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

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++){
            r1[i] = 1;
            r2[i] = 2;
        }
        for (int i = 0; i < 100; i++) System.out.print(" " + next());

    }

}
