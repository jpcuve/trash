/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 22, 2003
 * Time: 2:39:14 PM
 * To change this template use Options | File Templates.
 */
package net.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

public class Sink implements ByteChannel {
    private boolean open;

    public Sink() {
        this.open = true;
    }

    public boolean isOpen() {
        return open;
    }

    public void close() throws IOException {
        open = false;
    }

    public int read(ByteBuffer byteBuffer) throws IOException {
        int length = byteBuffer.remaining();
        byteBuffer.position(byteBuffer.limit());
        return length;
    }

    public int write(ByteBuffer byteBuffer) throws IOException {
        int length = byteBuffer.remaining();
        byteBuffer.position(byteBuffer.limit());
        return length;
    }
}
