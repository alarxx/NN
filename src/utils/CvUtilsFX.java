package utils;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;

/**
 * VM options: --module-path "путь к JavFX SDK\lib" --add-modules javafx.controls,javafx.fxml
 */

public class CvUtilsFX {

    //Mat to JavaFX
    public static WritableImage MatToWritableImage(Mat m) {
        BufferedImage bim = CvUtils.MatToBufferedImage(m);
        if (bim == null) return null;
        else return SwingFXUtils.toFXImage(bim, null);
    }
    //Mat to JavaFX
    public static WritableImage MatToImageFX(Mat m) {
        if (m == null || m.empty()) return null;
        if (m.depth() == CvType.CV_8U) {}
        else if (m.depth() == CvType.CV_16U) {
            Mat m_16 = new Mat();
            m.convertTo(m_16, CvType.CV_8U, 255.0 / 65535);
            m = m_16;
        }
        else if (m.depth() == CvType.CV_32F) {
            Mat m_32 = new Mat();
            m.convertTo(m_32, CvType.CV_8U, 255);
            m = m_32;
        }
        else
            return null;
        if (m.channels() == 1) {
            Mat m_bgra = new Mat();
            Imgproc.cvtColor(m, m_bgra, Imgproc.COLOR_GRAY2BGRA);
            m = m_bgra;
        }
        else if (m.channels() == 3) {
            Mat m_bgra = new Mat();
            Imgproc.cvtColor(m, m_bgra, Imgproc.COLOR_BGR2BGRA);
            m = m_bgra;
        }
        else if (m.channels() == 4) { }
        else
            return null;
        byte[] buf = new byte[m.channels() * m.cols() * m.rows()];
        m.get(0, 0, buf);
        WritableImage wim = new WritableImage(m.cols(), m.rows());
        PixelWriter pw = wim.getPixelWriter();
        pw.setPixels(0, 0, m.cols(), m.rows(),
                WritablePixelFormat.getByteBgraInstance(),
                buf, 0, m.cols() * 4);
        return wim;
    }

    public static Mat ImageFXToMat(Image img) {
        if (img == null) return new Mat();
        PixelReader pr = img.getPixelReader();
        int w = (int) img.getWidth();
        int h = (int) img.getHeight();
        byte[] buf = new byte[4 * w * h];
        pr.getPixels(0, 0, w, h, WritablePixelFormat.getByteBgraInstance(),
                buf, 0, w * 4);
        Mat m = new Mat(h, w, CvType.CV_8UC4);
        m.put(0, 0, buf);
        return m;
    }

    public static void showImage(Mat img, String title) {
        Image im = MatToImageFX(img);
        Stage window = new Stage();
        ScrollPane sp = new ScrollPane();
        ImageView iv = new ImageView();
        if (im != null) {
            iv.setImage(im);
            if (im.getWidth() < 1000) {
                sp.setPrefWidth(im.getWidth() + 5);
            }
            else sp.setPrefWidth(1000.0);
            if (im.getHeight() < 700) {
                sp.setPrefHeight(im.getHeight() + 5);
            }
            else sp.setPrefHeight(700.0);
        }
        sp.setContent(iv);
        sp.setPannable(true);
        BorderPane box = new BorderPane();
        box.setCenter(sp);
        Scene scene = new Scene(box);
        window.setScene(scene);
        window.setTitle(title);
        window.show();
    }


}
