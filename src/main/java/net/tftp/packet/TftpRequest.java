package net.tftp.packet;

import net.tftp.TftpPacket;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 9, 2006
 * Time: 1:40:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class TftpRequest extends TftpPacket {
    private String filename;
    public enum Mode{
        NETASCII, OCTET, MAIL
    };
    private Mode mode;

    public TftpRequest(Type type, String filename, Mode mode) {
        super(type);
        this.filename = filename;
        this.mode = mode;
    }
}
