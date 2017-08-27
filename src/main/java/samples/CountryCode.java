package samples;

import java.util.Scanner;

/**
 * Created by jpc on 13-11-16.
 */
public class CountryCode {
    public static void main(String[] args) throws Exception {
        final Scanner scanner = new Scanner(ClassLoader.getSystemResourceAsStream("countrycode/countrycode.csv"));
        String line;
        while ((line = scanner.nextLine()) != null){
            final String[] ss = line.split(",");
            final String countryName = ss[0];
            final String phoneCode = ss[8];
            System.out.printf("%s %s%n", countryName, phoneCode);
        }
        scanner.close();
    }
}
