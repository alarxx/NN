package OpenCVTest;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import utils.CvUtils;
import utils.CvUtilsFX;

import java.util.LinkedList;
import java.util.List;

public class Main extends Application{

    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {
        Application.launch(args);
    }
    public void start(Stage stage) throws Exception {
        VBox root = new VBox(15.0);
        root.setAlignment(Pos.CENTER);
        Button button = new Button("Выполнить");
        button.setOnAction(this::onClickButton);
        root.getChildren().add(button);
        Scene scene = new Scene(root, 400.0, 150.0);
        stage.setTitle("OpenCV " + Core.VERSION);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            Platform.exit();
        });

        stage.show();
    }

    private void onClickButton(ActionEvent e) {
        Mat f1 = openImg("C:\\Users\\Pupochek\\IdeaProjects\\NN\\assets\\frame100.jpg", "frame_1");
        Mat f2 = openImg("C:\\Users\\Pupochek\\IdeaProjects\\NN\\assets\\frame102.jpg", "frame_2");

    }


    public void computeKeyPoints(){
        Mat img = Imgcodecs.imread("C:\\Users\\Pupocheck\\IdeaProjects\\NN\\assets\\m1.jpg");
        Mat img2 = Imgcodecs.imread("C:\\Users\\Pupochek\\IdeaProjects\\NN\\assets\\m2.jpg");
        Imgproc.resize(img, img, new Size(720, 480));
        Imgproc.resize(img2, img2, new Size(720, 480));

        if (img.empty() || img2.empty()) {
            System.out.println("Не удалось загрузить изображения");
            return;
        }
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BGR2GRAY);

        // Находим ключевые точки
        MatOfKeyPoint kp_img = new MatOfKeyPoint();
        MatOfKeyPoint kp_img2 = new MatOfKeyPoint();
        ORB fd = ORB.create();
        fd.detect(img, kp_img);
        fd.detect(img2, kp_img2);

        // Отрисовываем найденные ключевые точки
        Mat result = new Mat();
        Features2d.drawKeypoints(img, kp_img, result,
                CvUtils.COLOR_WHITE, 0);
        CvUtilsFX.showImage(result, "result");
        Mat result2 = new Mat();
        Features2d.drawKeypoints(img2, kp_img2, result2,
                CvUtils.COLOR_WHITE, 0);
        CvUtilsFX.showImage(result2, "result2");

        // Вычисляем дескрипторы
        Mat descriptors_img = new Mat();
        Mat descriptors_img2 = new Mat();
        fd.compute(img, kp_img, descriptors_img);
        fd.compute(img2, kp_img2, descriptors_img2);

        // Сравниваем дескрипторы
        MatOfDMatch matches = new MatOfDMatch();
        DescriptorMatcher dm = DescriptorMatcher.create(
                DescriptorMatcher.BRUTEFORCE_HAMMING);
        dm.match(descriptors_img, descriptors_img2, matches);

        // Вычисляем минимальное и максимальное значения
        double max_dist = Double.MIN_VALUE, min_dist = Double.MAX_VALUE;
        float dist = 0;
        List<DMatch> list = matches.toList();
        for (int i = 0, j = list.size(); i < j; i++) {
            dist = list.get(i).distance;
            if (dist == 0) continue;
            if (dist < min_dist) min_dist = dist;
            if (dist > max_dist) max_dist = dist;
        }
        System.out.println("min = " + min_dist + " max = " + max_dist);

        // Находим лучшие совпадения
        LinkedList<DMatch> list_good = new LinkedList<DMatch>();
        for (int i = 0, j = list.size(); i < j; i++) {
            if (list.get(i).distance < min_dist * 3) {
                list_good.add(list.get(i));
            }
        }
        System.out.println(list_good.size());
        MatOfDMatch mat_good = new MatOfDMatch();
        mat_good.fromList(list_good);
// Отрисовываем результат
        Mat outImg = new Mat(img.rows() + img2.rows() + 10,
                img.cols() + img2.cols() + 10,
                CvType.CV_8UC3, CvUtils.COLOR_BLACK);
        Features2d.drawMatches(img, kp_img, img2, kp_img2, mat_good, outImg,
                new Scalar(255, 0, 0), Scalar.all(-1), new MatOfByte(),
                Features2d.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS);
        CvUtilsFX.showImage(outImg, "Результат сравнения");
        img.release(); img2.release();
        kp_img.release(); kp_img2.release();
        descriptors_img.release(); descriptors_img2.release();
        matches.release(); mat_good.release();
        result.release(); result2.release();
        outImg.release();
    }

    private void AKAZE(Mat img, String title){
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        MatOfKeyPoint kp = new MatOfKeyPoint();
        AKAZE fd = AKAZE.create();
        fd.detect(img, kp);
        Mat result = new Mat();
        Features2d.drawKeypoints(img, kp, result, CvUtils.COLOR_RED,
                Features2d.DrawMatchesFlags_DRAW_RICH_KEYPOINTS);
        CvUtilsFX.showImage(result, title);

    }

    private Mat openImg(String fileName, String title){
        Mat f1 = Imgcodecs.imread(fileName);
        Imgproc.resize(f1, f1, new Size(720, 480));
        CvUtilsFX.showImage(f1, title);
        return f1;
    }

    private Mat cornerHarris(Mat src){
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
        Mat dst = new Mat();
        Imgproc.cornerHarris(src, dst, 2, 3, 0.04);
        Core.MinMaxLocResult m = Core.minMaxLoc(dst);
        Imgproc.threshold(dst, dst, m.maxVal * 0.01, 1.0, Imgproc.THRESH_BINARY);
        CvUtilsFX.showImage(dst, "Corner Harris");
        return dst;
    }


}


