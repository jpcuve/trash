package samples;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jun 19, 2004
 * Time: 2:53:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class Enum {
    private enum Suit {
        hearts, clubs, diamonds, spades
    };

    public static void main(String[] args) {
        for (Suit suit :  Suit.values()) System.out.printf("suit: %s%n", suit);
    }
}
