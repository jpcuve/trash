package net.tftp.packet;

import net.tftp.TftpPacket;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 9, 2006
 * Time: 1:33:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TftpAcknowledgement extends TftpPacket {
    private int block;

    public TftpAcknowledgement(int block) {
        super(Type.ACK);
        this.block = block;
    }
}
