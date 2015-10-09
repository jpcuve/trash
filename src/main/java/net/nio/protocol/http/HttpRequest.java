package net.nio.protocol.http;

import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 25, 2005
 * Time: 4:20:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpRequest extends HttpMessage {
    private HttpMethod httpMethod;
    private String path;
    private String query;
    private int versionMajor;
    private int versionMinor;

    public HttpRequest(HttpHandler httpHandler, HttpMethod httpMethod, String path, String query, int versionMajor, int versionMinor) {
        super(httpHandler);
        this.httpMethod = httpMethod;
        this.path = path;
        this.query = query;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
    }

    public HttpRequest(HttpHandler httpHandler) {
        super(httpHandler);
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getVersionMajor() {
        return versionMajor;
    }

    public void setVersionMajor(int versionMajor) {
        this.versionMajor = versionMajor;
    }

    public int getVersionMinor() {
        return versionMinor;
    }

    public void setVersionMinor(int versionMinor) {
        this.versionMinor = versionMinor;
    }

    public void decodeLine() throws UnsupportedEncodingException, ProtocolException {
        if (line == null) throw new ProtocolException("not query line");
        httpMethod = HttpMethod.parse(line);
        if (httpMethod == null) throw new ProtocolException("unidentified http method");
        int sp1 = line.indexOf(' ');
        int sp2 = line.indexOf(' ', sp1 + 1);
        int qu = line.indexOf('?');
        if (sp1 == -1 || sp2 == -1) throw new ProtocolException("malformed http request line: " + line);
        path = URLDecoder.decode(qu != -1 ? line.substring(sp1 + 1, qu) : line.substring(sp1 + 1, sp2), Charset.defaultCharset().toString());
        query = URLDecoder.decode(qu != -1 ? line.substring(qu + 1, sp2) : "", Charset.defaultCharset().toString());
        int ht1 = line.indexOf("HTTP/");
        if (ht1 == -1) throw new ProtocolException("http protocol version not found");
        int dt1 = line.indexOf('/', ht1 + 1);
        if (dt1 == -1) throw new ProtocolException("http protocol major version number not found");
        int dt2 = line.indexOf('.', dt1 + 1);
        if (dt2 == -1) throw new ProtocolException("http protocol minor version number not found");
        versionMajor = Integer.parseInt(line.substring(dt1 + 1, dt2));
        versionMinor = Integer.parseInt(line.substring(dt2 + 1));
        setClose(!(versionMajor == 1 && versionMinor == 0));
    }

    public void encodeLine() throws UnsupportedEncodingException, ProtocolException {
        StringBuilder sb = new StringBuilder();
        if (httpMethod == null) throw new ProtocolException("no http method");
        sb.append(httpMethod);
        sb.append(' ');
        if (path == null || path.length() == 0) throw new ProtocolException("no path");
        StringTokenizer st = new StringTokenizer(path, "/", true);
        while (st.hasMoreElements()){
            String s = st.nextToken();
            sb.append(s.indexOf('/') == -1 ? URLEncoder.encode(s, Charset.defaultCharset().toString()): s);
        }
        if (query != null){
            sb.append('?');
            st = new StringTokenizer(query, "&=", true);
            while (st.hasMoreElements()){
                String s = st.nextToken();
                sb.append(s.indexOf('&') == -1 && s.indexOf('=') == -1 ? URLEncoder.encode(s, Charset.defaultCharset().toString()): s);
            }
        }
        sb.append(" HTTP/");
        sb.append(versionMajor);
        sb.append('.');
        sb.append(versionMinor);
        line = sb.toString();
    }

}
