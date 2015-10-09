package samples;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jpc
 */
public class HtmlConversion {
    public static final String[] HTMLS = new String[]{ "Ing. Loro Piana & c.;Loro Piana", "ME&#931;HMBPINH", "KPEM&#1051;EBKA", "dodoni A MO&#379;E MY JESTE&#346;MY DLA CIEBIE LEPSI COLA", "calypso lody &#347;mietankowe waniliowe LODMOR", "IZOKOR - P&#321;OCK S.A." };
    public static void main(String[] args) {
        final Pattern pattern = Pattern.compile("&#[0-9]+;");
        for (final String html: HTMLS){
            final StringBuffer sb = new StringBuffer();
            final Matcher matcher = pattern.matcher(html);
            while (matcher.find()) {
                final String group = matcher.group();
                matcher.appendReplacement(sb, Character.toString((char) Integer.parseInt(group.substring(2, group.length() - 1))));
            }
            matcher.appendTail(sb);
            System.out.printf("%s -> %s%n", html, sb.toString());
        }
    }
}
