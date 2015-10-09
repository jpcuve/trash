/*
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: 10-Jun-02
 * Time: 16:57:35
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package samples;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

public class Bomber {
    public static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DIGITS = "0123456789";
    public static final String[] STATES = {"AL","AK","AS","AZ","AR","CA","CO","CT","DE","DC","FM","FL","GA","GU","HI","ABSOLUTE","IL","IN","IA","KS","KY","LA","ME","MH","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","MP","OH","OK","OR","PW","PA","PR","RI","SC","SD","TN","TX","UT","VT","VI","VA","WA","WV","WI","WY"};
    public static final String[] GENDERS = {"m", "f"};

    private static String random(String chain, int size) {
        StringBuffer sb = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            sb.append(chain.charAt((int)(Math.random() * chain.length())));
        }
        return sb.toString();
    }

    private static String random(String[] choices) {
        return choices[(int)(Math.random() * choices.length)];
    }

    private static String getQueryString(HashMap h) {
        StringBuffer sb = new StringBuffer();
        for (Iterator i = h.keySet().iterator(); i.hasNext();) {
            sb.append(sb.length() == 0 ? "" : "&");
            String key = (String)i.next();
            sb.append(URLEncoder.encode(key) + "=" + URLEncoder.encode((String)h.get(key)));
        }
        return sb.toString();

    }

    public static void main(String[] args)
    throws Exception {
        HashMap map = new HashMap();
        URL url = new URL("http://www.cuttingedgeoffers.com/images/index2.asp");
        for (int i = 0; i < 10; i++) {
            map.clear();
            map.put("email2", random(CHARS, 5) + "@" + random(CHARS, 8) + ".com");
            map.put("fname2", random(CHARS, 7));
            map.put("lname2", random(CHARS, 12));
            map.put("addr12", random(CHARS, 24));
            map.put("addr22", random(CHARS, 24));
            map.put("city2", random(CHARS, 16));
            map.put("select", random(STATES));
            map.put("zip2", random(DIGITS, 5));
            map.put("gender", random(GENDERS));
            System.out.println(getQueryString(map));
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.connect();
            OutputStream out = conn.getOutputStream();
            PrintStream ps = new PrintStream(out);
            ps.print(getQueryString(map));
            ps.flush();
            ps.close();
            int res = conn.getResponseCode();
            System.out.println("Response code=" + res);
            conn.disconnect();
        }

    }
}
