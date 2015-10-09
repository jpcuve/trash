package net.nio.protocol.line;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 26, 2005
 * Time: 9:57:24 AM
 * To change this template use File | Settings | File Templates.
 */
public interface LineHandler {
    void lineRead(String line);
    void lineWritten(String line);
}
