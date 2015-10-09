package net.nio.protocol.http;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 25, 2005
 * Time: 5:19:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HttpHandler {
    void lineRead(String line);
    void headerRead(HttpHeader header);
    void headersRead();
    void dataRead();
    void lineWritten(String line);
    void headerWritten(HttpHeader header);
    void headersWritten();
    void dataWritten();
}
