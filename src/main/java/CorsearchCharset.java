import java.nio.charset.StandardCharsets;
import java.text.Collator;

public class CorsearchCharset {
    public static byte[] NAME_1 = {0x50,0x49,0x2D,0x44,0x65,0x73,0x69,0x67,0x6E};
    public static byte[] NAME_2 = {0x50,0x49,0x2D,0x44,0x65,0x73,0x69,0x67,0x6E,
            (byte) 0xE2,(byte) 0x80,(byte) 0x86,(byte) 0xE2,(byte) 0x80,(byte) 0x86};

    public static void main(String[] args) {
        var nameOne = new String(NAME_1, StandardCharsets.UTF_8);
        var nameTwo = new String(NAME_2, StandardCharsets.UTF_8);
        System.out.printf("|%s|%n", nameOne);
        System.out.printf("|%s|%n", nameTwo);
        Collator collator = Collator.getInstance();
        System.out.println(collator.getCollationKey(nameOne));
        int result = collator.compare(nameOne, nameTwo);
        System.out.printf("%s%n", result);
        result = nameOne.compareToIgnoreCase(nameTwo);
        System.out.printf("%s%n", result);

    }
}
