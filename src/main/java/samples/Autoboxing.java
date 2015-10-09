package samples;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jun 19, 2004
 * Time: 1:28:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Autoboxing {
    public static void main(String[] args) {
        Integer integer = 1;
        System.out.printf("integer=%d%n", integer);
        int i = integer + 2;
        System.out.printf("i=%d%n", i);
    }
}
