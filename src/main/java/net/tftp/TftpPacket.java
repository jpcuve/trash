package net.tftp;

import net.tftp.packet.*;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Jan 9, 2006
 * Time: 1:27:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TftpPacket {
    public enum Type {
        RRQ, WRQ, DATA, ACK, ERROR
    };

    private Type type;

    public TftpPacket(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public static TftpPacket parse(byte[] data, int offset, int length){
        int opcode = data[offset] << 8 + data[offset + 1];
        switch(opcode){
            case 1:
                return new TftpReadRequest("/filename", TftpRequest.Mode.OCTET);
            case 2:
                return new TftpWriteRequest("/filename", TftpRequest.Mode.OCTET);
            case 3:
                return new TftpData(0, new byte[]{ 1, 2, 3, 4 });
            case 4:
                return new TftpAcknowledgement(0);
            case 5:
                return new TftpError(123, "some error");
            default:
                return null;
        }
    }

}
