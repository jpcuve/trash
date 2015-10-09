package samples;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Feb 23, 2009
 * Time: 11:01:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class DateTimeParser2 {
    public static final Pattern[] PATTERNS = {
            Pattern.compile("~\\d{4}-\\d{2}-\\d{2}~"),
            Pattern.compile("~\\d{4}-\\d{2}-\\d{2}[+|-]\\d{2}\\d{2}~"),
            Pattern.compile("~\\d{2}:\\d{2}:\\d{2}~"),
            Pattern.compile("~\\d{2}:\\d{2}:\\d{2}\\.\\d+~")
    };
    
    public static final String[] TESTS = {
            "~2009-02-28~",
            "~2009-02-26+0200~",
            "~11:15:33~",
            "~11:13:02.5-0430~",
            "~2009-02-28T11:15:33~",
            "~2009-02-28T11:15:33.58~",
            "~2009-02-28T11:15:33+0800~",
            "~2009-02-28T11:15:33.987-0730~",
            "~2009-02-28TAB~"

    };

    public static void main(String[] args) {
        for (final String test: TESTS){
            for (final Pattern pattern: PATTERNS){
                if (pattern.matcher(test).matches()) System.out.printf("%s -> %s%n", pattern, test);
            }
        }
        

    }
}
