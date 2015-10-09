package lang.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created with IntelliJ IDEA.
 * User: jpc
 * Date: 3/5/13
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class BigDecimalTest {
    public static void main(String[] args) {
        for (final String s: new String[] { "0.006", "0.004"}){
            final BigDecimal bd1 = new BigDecimal(s);
            final BigDecimal bd2 = bd1.setScale(2, RoundingMode.HALF_EVEN);
            System.out.println(bd2.signum() == 0);

        }
    }
}
