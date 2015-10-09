package net.tftp.packet;

import net.tftp.TftpPacket;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 9, 2006
 * Time: 1:33:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class TftpData extends TftpPacket {
    private int block;
    private byte[] data;
    public TftpData(int block, byte[] data) {
        super(Type.DATA);
        this.block = block;
        this.data = data;
    }
}
