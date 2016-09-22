package parser.spindizzy;

import java.io.*;

/**
 * Created by jpc on 21/11/2015.
 */
public class Spindizzy {
    private final static byte[] BUFFER = new byte[2 * 1024 * 1024];

    public static void brute(int length) throws IOException {
        // scan for brushesBrute 3759

        // scan for screens
        int screenCount = 0;
        for (int i = 4247; i < length; i++){
            int l = BUFFER[i] & 0xFF;
            if (l > 6){
                if ((BUFFER[i + 1] & 0x80) == 0 && (BUFFER[i + 2] & 0x80) == 0 && (BUFFER[i + 3] & 0xD0) == 0){
                    int y = BUFFER[i + 1] & 0x7F;
                    int x = BUFFER[i + 2] & 0x7F;
                    int floor = BUFFER[i + 4] & 0xFF;
                    // floor either basic block or column of basic blocks
                    if (x > 0 && y > 0 && floor > 0){
                        System.out.printf("Screen at: 0x%X, length: %s, (%s,%s) next: 0x%X%n", i, l, x, y, i + l);
                        screenCount++;
                        i += (l - 1);
                    }

                }

            }
        }
        System.out.printf("screen count: %s%n", screenCount);
    }

    public static void brushesBrute() throws IOException {
        for (int i = 0; i < 0x10000; i++){
            int start = i; // 0xEAF
            int brushCount = 0;
            int length;
            while((length = (BUFFER[start + 1] & 0xFF)) != 0){
                start += length;
                brushCount++;
            }
            if (brushCount > 19 && start <= 0x1097){
                System.out.printf("Brush count: %s for start-end: 0x%X-0x%X%n", brushCount, i, start);
            }
        }
    }

    public static void screens() throws IOException {
        final char[][] set = new char[128][128];
        for (int i = 0; i < 128; i++){
            for (int j = 0; j < 128; j++){
                set[i][j] = ' ';
            }
        }
        int start = 0x1097;
        int screenCount = 0;
        int length;
        while((length = BUFFER[start]) != 0){
            start += length;
            int y = BUFFER[start + 1] & 0x7F;
            int x = BUFFER[start + 2] & 0x7F;
            set[x][y] = '*';
            System.out.println(length);
            screenCount++;
        }
        System.out.printf("Screen count: %s%n", screenCount);
        for (int i = 0; i < 128; i++){
            for (int j = 0; j < 128; j++){
                System.out.print(set[j][i]);
            }
            System.out.println();
        }
        final FileOutputStream fos = new FileOutputStream("etc/spindizzy/spindizzy_screens.bin");
        fos.write(BUFFER, 0x1097, start - 0x1097);
        fos.flush();
        fos.close();

    }

    public static void brushes() throws IOException {
        final FileOutputStream fos = new FileOutputStream("etc/spindizzy/spindizzy_brushes.bin");
        int start = 0x8A0;
        int brushCount = 0;
        int length;
        while((length = (BUFFER[start + 1] & 0xFF)) != 0){
            if (BUFFER[start + length - 1] == 0 && BUFFER[start + length - 2] == 0){
                System.out.printf("Brush id: %s 0x%X%n", BUFFER[start] & 0xFF, start);
                fos.write(BUFFER, start, length);
            }
            start += length;
            brushCount++;
        }
        System.out.printf("Brush count: %s%n", brushCount);
        fos.flush();
        fos.close();
    }

    public static void outputBrushesAsJson() throws IOException{
        final InputStream is = new FileInputStream("etc/spindizzy/spindizzy_brushes.bin");
        final byte[] buffer = new byte[4096];
        int read, length = 0;
        while ((read = is.read(buffer, length, buffer.length - length)) >= 0){
            length += read;
        }
        System.out.printf("bytes read: %s%n", length);
        is.close();
        int start = 0;
        int brushCount = 0;
        while((length = (buffer[start + 1] & 0xFF)) != 0){
            System.out.printf("Brush id: %s 0x%X%n", buffer[start] & 0xFF, start);
            start += length;
            brushCount++;
        }
        System.out.printf("Brush count: %s%n", brushCount);
    }

    public static void outputScreensAsJson() throws IOException {
        final InputStream is = new FileInputStream("etc/spindizzy/spindizzy_screens.bin");
        final byte[] buffer = new byte[16384];
        int read, length = 0;
        while ((read = is.read(buffer, length, buffer.length - length)) >= 0){
            length += read;
        }
        System.out.printf("bytes read: %s%n", length);
        is.close();
        int start = 0;
        int screenCount = 0;
        while((length = (buffer[start] & 0xFF)) != 0){
            start += length;
            screenCount++;
        }
        System.out.printf("Screen count: %s%n", screenCount);
    }



    public static void main(String[] args) throws IOException {
        final InputStream is = ClassLoader.getSystemResourceAsStream("spindizzy/freeze.c64");
        int read, length = 0;
        while ((read = is.read(BUFFER, length, BUFFER.length - length)) >= 0){
            length += read;
        }
        System.out.printf("File length: %s%n", length);
        brushes();
/*
        outputBrushesAsJson();
        outputScreensAsJson();
*/


    }
}
