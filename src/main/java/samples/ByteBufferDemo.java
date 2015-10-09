package samples;

import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Dec 16, 2004
 * Time: 1:44:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ByteBufferDemo {
    private static void dump(ByteBuffer byteBuffer){
        System.out.printf("position=%s, limit=%s, capacity=%s, remaining=%s, hasRemainig=%s\n", byteBuffer.position(), byteBuffer.limit(), byteBuffer.capacity(), byteBuffer.remaining(), byteBuffer.hasRemaining());

    }

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8);
        dump(byteBuffer);
        System.out.println("put 3 bytes");
        for (int i = 0; i < 3; i++) byteBuffer.put((byte)1);
        dump(byteBuffer);
        System.out.println("flip");
        byteBuffer.flip();
        dump(byteBuffer);
        System.out.println("get 2 bytes");
        for (int i = 0; i < 2; i++) byteBuffer.get();
        dump(byteBuffer);
        System.out.println("compact");
        byteBuffer.compact();
        dump(byteBuffer);

    }
}
