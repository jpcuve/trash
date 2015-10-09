package graphic.cities;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 2, 2010
 * Time: 1:21:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Cities {
    private static class City{
        public long id;
        public String nameEnglish;
        public String nameAscii;
        public String nameOther;
        public BigDecimal longitude;
        public BigDecimal latitude;
        public String country;
        public int population;
        public String timezone;
        public Date sample;

        private City(long id, String nameEnglish, String nameAscii, String nameOther, BigDecimal longitude, BigDecimal latitude, String country, int population, String timezone, Date sample) {
            this.id = id;
            this.nameEnglish = nameEnglish;
            this.nameAscii = nameAscii;
            this.nameOther = nameOther;
            this.longitude = longitude;
            this.latitude = latitude;
            this.country = country;
            this.population = population;
            this.timezone = timezone;
            this.sample = sample;
        }

        @Override
        public String toString() {
            return String.format("%s: %s %s %s (%s) [%s, %s]", id, country, nameEnglish, population, nameOther, longitude, latitude);
        }
    }

    public static void main(String[] args) throws Exception {
        final LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream("cities15000.txt"), Charset.forName("UTF-8")));
        String line;
        while ((line = lnr.readLine()) != null){
            final String[] fields = line.split("\\t");
            final long id = Long.parseLong(fields[0]);
            final String nameEnglish = fields[1];
            final String nameAscii = fields[2];
            final String nameOther = fields[3];
            final BigDecimal longitude = new BigDecimal(fields[4]);
            final BigDecimal latitude = new BigDecimal(fields[5]);
            final String country = fields[8];
            final int population = Integer.parseInt(fields[14]);
            final String timezone = fields[17];
            final Date sample = new SimpleDateFormat("yyyy-MM-dd").parse(fields[18]);

            final City city = new City(id, nameEnglish, nameAscii, nameOther, longitude, latitude, country, population, timezone, sample);
            if (city.population > 1000000) System.out.println(city);
        }
    }
}
