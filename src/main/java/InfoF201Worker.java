import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class InfoF201Worker implements Runnable {
    private static final int STOP_BYTE = '\t';
    private final Socket socket;
    private final byte[] buffer = new byte[1024];
    private int ptr;
    private int year;
    private int month;
    private int day;
    private String filename;

    public InfoF201Worker(Socket socket)  {
        this.socket = socket;
    }

    private void checkSize(int maxLength) throws IOException {
        if (ptr > maxLength){
            throw new IOException(String.format("Input too long: %s", new String(buffer, 0, ptr, StandardCharsets.UTF_8)));
        }
    }

    private String reset(){
        String s = new String(buffer, 0, ptr, StandardCharsets.UTF_8);
        ptr = 0;
        return s;
    }

    @Override
    public void run() {
        int state = 0;
        reset();
        try (socket) {
            while (state < 5) {
                int read = socket.getInputStream().read();
                switch (state) {
                    case 0:
                        if (read != STOP_BYTE) {
                            buffer[ptr++] = (byte) read;
                            checkSize(4);
                        } else {
                            year = Integer.parseInt(reset());
                            state = 1;
                        }
                        break;
                    case 1:
                        if (read != STOP_BYTE) {
                            buffer[ptr++] = (byte) read;
                            checkSize(2);
                        } else {
                            month = Integer.parseInt(reset());
                            state = 2;
                            ptr = 0;
                        }
                        break;
                    case 2:
                        if (read != STOP_BYTE) {
                            buffer[ptr++] = (byte) read;
                            checkSize(2);
                        } else {
                            day = Integer.parseInt(reset());
                            state = 3;
                            ptr = 0;
                        }
                        break;
                    case 3:
                        if (read != STOP_BYTE) {
                            buffer[ptr++] = (byte) read;
                            checkSize(buffer.length);
                        } else {
                            filename = reset();
                            state = 4;
                            ptr = 0;
                        }
                        break;
                    case 4:
                        System.out.printf("%s %s %s %s %n", year, month, day, filename);
                        state = 5;
                        break;
                }

            }
        } catch (NumberFormatException|IOException e){
            e.printStackTrace();
        }
    }
}
