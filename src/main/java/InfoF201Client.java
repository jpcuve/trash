import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class InfoF201Client {
    public static void main(String[] args) throws IOException {
        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(5555));
            socket.getOutputStream().write("2018\t12\t05\tNom de fichier\t".getBytes(StandardCharsets.UTF_8));
        }
    }
}
