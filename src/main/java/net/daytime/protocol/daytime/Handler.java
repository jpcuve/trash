package net.daytime.protocol.daytime;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 6, 2006
 * Time: 8:40:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Handler extends URLStreamHandler {
    protected int getDefaultPort() {
        return 13;
    }

    protected URLConnection openConnection(URL u) throws IOException {
        return new DaytimeURLConnection(u);
    }
}
