package net.nio.protocol.http;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 27, 2005
 * Time: 2:07:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpHeader {
    private String key;
    private String value;

    public HttpHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append(": ");
        sb.append(value);
        return sb.toString();
    }

    public static String encode(HttpHeader header){
        return header.toString();
    }

    public static HttpHeader decode(String line){
        HttpHeader httpHeader = new HttpHeader(null, null);
        int l1 = line.indexOf(':');
        httpHeader.key = (l1 != -1) ? line.substring(0, l1).trim() : line;
        httpHeader.value = (l1 != -1) ? line.substring(l1 + 1).trim() : "";
        return httpHeader;
    }

}
