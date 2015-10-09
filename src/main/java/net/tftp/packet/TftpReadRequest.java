package net.tftp.packet;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 9, 2006
 * Time: 1:32:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class TftpReadRequest extends TftpRequest {

    public TftpReadRequest(String filename, Mode mode) {
        super(Type.RRQ, filename, mode);
    }
}
