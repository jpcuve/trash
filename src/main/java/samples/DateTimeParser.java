package samples;

import java.text.ParseException;
import java.util.Date;

public class DateTimeParser {
    public static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private String text;
    private StringBuilder sb = new StringBuilder();
    private int pos;

    public static final String[] TESTS = {
            "2009-02-28",
            "2009-02-26+0200",
            "11:15:33",
            "11:13:02.544-0430",
            "2009-02-28T11:15:33",
            "2009-02-28T11:15:33.585",
            "2009-02-28T11:15:33+0800",
            "2009-02-28T11:15:33.987-0730",
            "2009-02-28TAB"
    };

    private char read(){
        if (pos >= text.length()) return 0;
        char c = text.charAt(pos++);
        sb.append(c);
        return c;
    }

    private void reset(){
        sb.setLength(0);
        pos = 0;
    }

    private void back(int n){
        if (pos >= n){
            pos = pos - n;
            sb.setLength(sb.length() - n);
        }
    }

    private void back(){
        back(1);
    }

    private int pickup(int length, int offset){
        return Integer.parseInt(sb.substring(sb.length() - length - offset, sb.length() - offset));
    }


    private Object scan(final String s){
        int yyyy = -1;
        int MM = -1;
        int dd = -1;
        int HH = -1;
        int mm = -1;
        int ss = -1;
        int SSS = -1;
        boolean pm = false;
        int z1 = -1;
        int z2 = -1;

        this.text = s;
        int state = 0;

        char c;
        while (state != 666){
            switch(state){
                case 0: // try time first
                    reset();
                    for (int i = 0; i < 2; i++) if (!Character.isDigit(read())) return null;
                    c = read();
                    if (c == ':'){
                        HH = pickup(2, 1);
                        state = 10; // continue time
                    } else{
                        if (!Character.isDigit(read())) return null;
                        c = read();
                        if (c == '-'){
                            yyyy = pickup(4, 1);
                            state = 20; // continue date
                        } else{
                            return null;
                        }
                    }
                    break;
                case 10: // continue time
                    // need at least hour and min
                    for (int i = 0; i < 2; i++) if (!Character.isDigit(read())) return null;
                    mm = pickup(2, 0);
                    ss = 0;
                    SSS = 0;
                    c = read();
                    switch(c){
                        case ':':
                            state = 11; // seconds
                            break;
                        case '.':
                            state = 12; // milliseconds
                            break;
                        case '+':
                        case '-':
                            state = 13; // time zone
                            break;
                        case 0:
                            state = 999;
                            break;
                        default:
                            back();
                            state = 999;
                            break;
                    }
                    break;
                case 11:
                    for (int i = 0; i < 2; i++) if (!Character.isDigit(read())) return null;
                    ss = pickup(2, 0);
                    c = read();
                    switch(c){
                        case '.':
                            state = 12; // milliseconds
                            break;
                        case '+':
                        case '-':
                            state = 13; // time zone
                            break;
                        case 0:
                            state = 999;
                            break;
                        default:
                            back();
                            state = 999;
                            break;
                    }
                    break;
                case 12:
                    for (int i = 0; i < 3; i++) if (!Character.isDigit(read())) return null;
                    SSS = pickup(3, 0);
                    c = read();
                    switch(c){
                        case '+':
                        case '-':
                            state = 13;
                            break;
                        case 0:
                            state = 999;
                            break;
                        default:
                            back();
                            state = 999;
                            break;
                    }
                    break;
                case 13:
                    pm = '+' == sb.charAt(sb.length() - 1);
                    for (int i = 0; i < 4; i++) if (!Character.isDigit(read())) return null;
                    z1 = pickup(2, 2);
                    z2 = pickup(2, 0);
                    state = 999;
                    break;
                case 20: // continue date
                    for (int i = 0; i < 2; i++) if (!Character.isDigit(read())) return null;
                    MM = pickup(2, 0);
                    if ('-' != read()) return null;
                    for (int i = 0; i < 2; i++) if (!Character.isDigit(read())) return null;
                    dd = pickup(2, 0);
                    c = read();
                    switch(c){
                        case 'T':
                        case 't':
                            for (int i = 0; i < 2; i++) if (!Character.isDigit(read())) return null;
                            HH = pickup(2, 0);
                            if (':' != read()) return null;
                            state = 10; // continue time
                            break;
                        case '+':
                        case '-':
                            state = 13; // time zone
                            break;
                        case 0:
                            state = 999;
                            break;
                        default:
                            back();
                            state = 999;
                            break;
                    }
                    break;
                case 999:
                    System.out.printf("yyyy:%s MM:%s dd:%s HH:%s mm:%s ss:%s SSS:%s pm:%s z1:%s z2:%s %n", yyyy, MM, dd, HH, mm, ss, SSS, pm, z1, z2);
                    return null;
            }
        }
        return null;
    }

    public String getText(){
        return sb.toString();
    }

    public static Date parse(final String s) throws ParseException {
        final DateTimeParser parser = new DateTimeParser();
        parser.scan(s);
        return new Date();
    }

    public static void main(String[] args) throws Exception {
        for (final String test: TESTS){
            parse(test);
            System.out.printf("ok: %s%n", test);
        }
    }
}