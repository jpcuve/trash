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
        // on démarre de l'état zéro, correspondant à la lecture de l'année.
        int state = 0;
        reset();
        try (socket) {
            while (state < 5) { // l'état 5 correspond à l'état de sortie du worker
                int read = socket.getInputStream().read(); // lire le caractère (en java le byte est délivré comme int
                // valeur 0 à 255, -1 correspond à end-of-stream). Cette methode est bloquante, le timeout a été
                // défini comme paramètre du serveur
                switch (state) {
                    case 0: // lecture de l'année
                        if (read != STOP_BYTE) {
                            // tant qu'on est pas arrivé au stop byte, on accumule les caractères lu dans un buffer
                            buffer[ptr++] = (byte) read;
                            // avec ici la limite de 4 caractères max pour l'année
                            checkSize(4);
                        } else {
                            // ici, on a fini d'accumuler les caractères de l'année
                            // on convertit l'année en int et on la stocke dans une variable locale au worker
                            // (en C ce sera peut être une variable locale au thread du worker)
                            year = Integer.parseInt(reset());
                            // et on passe à l'état 1, lecture du  mois
                            state = 1;
                        }
                        break;
                    case 1: // lecture du mois, etc.
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
