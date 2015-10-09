package net.nio.protocol.ftp;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 28, 2005
 * Time: 2:14:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FtpHandler {
    void requestRead(FtpMethod method, String parameter);
    void responseWritten(int code, String[] lines);
    void dataTransferComplete();
    void dataTransferFailed(Exception x);
}
