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
            System.out.println(ss);
            break;
        }
        scanner.close();
    }
}
