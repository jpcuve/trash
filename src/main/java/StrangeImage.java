import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URL;

public class StrangeImage {
    public static void main(String[] args) throws Exception {
        BufferedImage image1 = ImageIO.read(new File("c:/Users/jpc/Desktop/GGIVU4WGEGD2LJBLYJY6ACUKPYZRFSKDTNU72DSNSWOLH2XP3BKTQG2BEOIOG6DRUQ762MLSBNPAA.jpg"));
        System.out.println(image1);
        BufferedImage image2 = ImageIO.read(new URL("https://euipo.europa.eu/copla/thumbnail/GGIVU4WGEGD2LJBLYJY6ACUKPYZRFSKDTNU72DSNSWOLH2XP3BKTQG2BEOIOG6DRUQ762MLSBNPAA"));
        System.out.println(image2);
    }
}
