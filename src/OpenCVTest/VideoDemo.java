package OpenCVTest;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import utils.CvUtils;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class VideoDemo {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        JFrame window = new JFrame("Просмотр видео");
        window.setSize(1000, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        JLabel label = new JLabel();
        window.setContentPane(label);
        window.setVisible(true);

        VideoCapture capture = new VideoCapture("C:\\Users\\Pupochek\\IdeaProjects\\NN\\assets\\car.mp4");

        if (!capture.isOpened()) {
            System.out.println("Не удалось открыть видео");
            return;
        }else {
            System.out.println("Открыт видеофайл");
            System.out.println("Кол-во кадров: " + capture.get(Videoio.CAP_PROP_FRAME_COUNT));
            System.out.println("FPS: " +  capture.get(Videoio.CAP_PROP_FPS));
        }

        Mat frame = new Mat();
        BufferedImage img = null;
        int ff = 0;
        while (capture.read(frame)) {
            ff++;
            if(ff==100 || ff==101)
                Imgcodecs.imwrite("C:\\Users\\Pupochek\\IdeaProjects\\NN\\assets\\frame"+ff+".jpg", frame);

            Imgproc.resize(frame, frame, new Size(960, 540));
            // Здесь можно вставить код обработки кадра
            img = CvUtils.MatToBufferedImage(frame);
            if (img != null) {
                ImageIcon imageIcon = new ImageIcon(img);
                label.setIcon(imageIcon);
                label.repaint();
                window.pack();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Выход");
        capture.release();
    }
}
