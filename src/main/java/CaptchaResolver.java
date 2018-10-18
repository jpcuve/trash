import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.features2d.MSER;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CaptchaResolver {
    public static void main(String[] args) throws Exception {
/*
        for (int i = 0; i < 20; i++){
            RenderedImage image = ImageIO.read(new URL("http://bpm.roamwifi.com/login/generateAuthCode"));
            ImageIO.write(image, "png", new File(String.format("image_%s.png", i)));
        }
*/
        OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("mat = " + mat.dump());
        Files.walk(Paths.get("src/main/resources/captcha"))
                .filter(path -> path.getFileName().toString().endsWith(".png"))
                .forEach(path -> {
                    try{
                        BufferedImage img = ImageIO.read(path.toFile());
                        System.out.println(img);
                        Mat mi = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
                        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
                        mi.put(0, 0, pixels); // now image is matrix
                        Mat mo1 = new Mat(mi.rows(), mi.cols(), CvType.CV_8U);
                        Imgproc.cvtColor(mi, mo1, Imgproc.COLOR_BGR2GRAY);
                        Mat mo2 = mo1.clone();
                        Imgproc.blur(mo1, mo2, new Size(3, 3));
                        Mat mo3 = mo2.clone();
                        Imgproc.threshold(mo2, mo3, 128, 250, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
                        // cvtColor(mi, mo, Imgproc.COLOR_BGR2GRAY);
                        MSER mser = MSER.create();
                        mser.setMaxArea(img.getWidth() * img.getHeight() / 4);
/*
                        mser.setMaxArea(img.getHeight() / 2);
                        mser.setMinArea(img.getHeight() / 10);
*/
                        List<MatOfPoint> msers = new ArrayList<>();
                        MatOfRect matOfRect = new MatOfRect();
                        mser.detectRegions(mo3, msers, matOfRect);
                        for (Rect rect: matOfRect.toList()){
                            Imgproc.rectangle(
                                    mo3,
                                    new Point(rect.x, rect.y),
                                    new Point(rect.x + rect.width, rect.y + rect.height),
                                    new Scalar(1, 0, 0), 1);
                        }
                        MatOfByte matOfByte = new MatOfByte();
                        Imgcodecs.imencode(".jpg", mo3, matOfByte);
                        BufferedImage outImage = ImageIO.read(new ByteArrayInputStream(matOfByte.toArray()));
                        ImageIO.write(outImage, "jpg", new File(path.toAbsolutePath().toString() + ".jpg"));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                });
    }
}
