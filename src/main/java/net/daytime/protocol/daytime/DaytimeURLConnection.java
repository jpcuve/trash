package net.daytime.protocol.daytime;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 6, 2006
 * Time: 4:21:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class DaytimeURLConnection extends URLConnection {
    private Socket socket;

    public DaytimeURLConnection(URL url) {
        super(url);
    }

    public synchronized InputStream getInputStream() throws IOException {
        if (socket == null) connect();
        return socket.getInputStream();
    }

    public String getContentType() {
        return "application/x-daytime";
    }

    public synchronized void connect() throws IOException {
        socket = new Socket(url.getHost(), url.getPort());
    }

    public synchronized void disconnect() throws IOException {
        if (socket != null) try{
            socket.close();
        } finally{
            socket = null;
        }
    }
}
