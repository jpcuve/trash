import java.util.regex.Pattern;

public class XmlIllegal {
    private final static String XML_10 = "[^\u0009\r\n\u0020-\uD7FF\uE000-\uFFFD\ud800\udc00-\udbff\udfff]";

    public static void main(String[] args) {
        final Pattern pattern = Pattern.compile(XML_10);
        String illegal = "Hello,\b World!\0";
        String legal = illegal.replaceAll(XML_10, "");
        System.out.println(legal);
        System.out.println(pattern.matcher(illegal).replaceAll(""));
    }
}
