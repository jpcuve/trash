package samples;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jul 18, 2008
 * Time: 9:50:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class Sort {
    public static final String[] CITIES = new String[]{ "Ávila", "Augsburg", "Berlin" };
    public static void main(String[] args) {
        final List<String> list = Arrays.asList(CITIES);
        Collections.sort(list, Collator.getInstance(Locale.ENGLISH));
        for (final String city: list) System.out.printf("%s%n", city);
    }
}
