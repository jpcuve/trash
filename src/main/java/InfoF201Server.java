import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InfoF201Server {
    public static void main(String[] args) throws IOException  {
        try(ServerSocket serverSocket = new ServerSocket(5555)){
            while(true){
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(10_000); // temps maximum d'attente du caractère suivant sur le socket
                new Thread(new InfoF201Worker(socket)).start();
            }
        }
    }
}
