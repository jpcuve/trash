package swing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;

/**
 * Created by jpc on 30-11-16.
 */
public class BlurImage {
    public static final int KERNEL_SIZE = 20;
    public static void main(String[] args) throws Exception {
        BufferedImage bufferedImage = ImageIO.read(new File("etc/test.png"));
        final Graphics2D g2d = bufferedImage.createGraphics();
        final float[] matrix = new float[KERNEL_SIZE * KERNEL_SIZE];
        float v = 1f / matrix.length;
        for (int i = 0; i < matrix.length; i++){
            matrix[i] = v;
        }
        final Kernel kernel = new Kernel(KERNEL_SIZE, KERNEL_SIZE, matrix);
        BufferedImageOp op = new ConvolveOp(kernel);
        BufferedImage out = op.filter(bufferedImage, null);
        g2d.drawImage(out, 100, 100, 200, 200, 100, 100, 200, 200, null);
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel(new ImageIcon(bufferedImage)));
        frame.pack();
        frame.setVisible(true);
    }
}
