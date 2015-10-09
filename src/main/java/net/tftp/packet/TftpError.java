package net.tftp.packet;

import net.tftp.TftpPacket;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 9, 2006
 * Time: 1:33:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class TftpError extends TftpPacket {
    private int errorCode;
    private String errorMessage;

    public TftpError(int errorCode, String errorMessage) {
        super(Type.ERROR);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
