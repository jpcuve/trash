package net.tftp.packet;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 9, 2006
 * Time: 1:32:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class TftpWriteRequest extends TftpRequest {
    public TftpWriteRequest(String filename, Mode mode) {
        super(Type.WRQ, filename, mode);
    }
}
