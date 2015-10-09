package samples;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jun 19, 2004
 * Time: 2:32:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Generics {
    public static void main(String[] args) {
        LinkedList<String> strings = new LinkedList<String>();
        strings.add("hello");
        strings.add("world");
        strings.add("!");
        for (String s : strings){
            System.out.printf("%s ", s);
        }

    }
}
