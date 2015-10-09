package samples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: 6/6/11
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class OpenFiles {
    public static void main(String[] args) {
        int i = 0;
        try{
            for (i = 0; i < 80000; i++){
                final File f = File.createTempFile("test", "bin");
                final FileOutputStream fos = new FileOutputStream(f);
                fos.write(0);
//            fos.flush();
            }

        } catch(IOException x){
            System.out.println("i=" + i);
        }
    }
}
