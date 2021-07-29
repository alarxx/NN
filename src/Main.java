import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import utils.CvUtilsFX;

public class Main extends Application{

    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    /*public static void main(String[] args) {
        System.out.println("Welcome to OpenCV " + Core.VERSION);
        Mat m  = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("m = " + m.dump());
    }

     */

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
        Mat src = Imgcodecs.imread("C:\\Users\\Pupocheck\\IdeaProjects\\NN\\assets\\pic.jpg");

        Imgproc.resize(src,src,new Size(800, 600));
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY);

        //Creating an empty matrix to store the result
        Mat dstX = new Mat();
        Mat dstY = new Mat();
        Mat dst = new Mat();

        // Creating kernel matrix
        Mat kX = Mat.ones(3,3, CvType.CV_32FC1);
        Mat kY = Mat.ones(3,3, CvType.CV_32FC1);

        double[] kernelX = new double[]{-1, 0, 1, -2, 0, 2, -1, 0, 1};
        double[] kernelY = new double[]{-1, -2, -1, 0, 0, 0, 1, 2, 1};

        kX.put(0, 0, kernelX);
        kY.put(0, 0, kernelY);

        System.out.println("X: " + kX.dump());
        System.out.println("Y: " + kY.dump());

        Imgproc.filter2D(src, dstX, CvType.CV_64FC1, kX);
        Imgproc.filter2D(src, dstY, CvType.CV_64FC1, kY);

        Core.pow(dstX, 2, dstX);
        Core.pow(dstY, 2, dstY);
        Core.add(dstX, dstY, dst);
        Core.sqrt(dst, dst);
        Core.divide(dst, new Scalar(1442.4978), dst, 255);

        dstX.convertTo(dstX, CvType.CV_8UC1);
        dstY.convertTo(dstY, CvType.CV_8UC1);
        dst.convertTo(dst, CvType.CV_8UC1);

        // Отображаем в отдельном окне
        CvUtilsFX.showImage(src, "src");
        CvUtilsFX.showImage(dstX, "dstX");
        CvUtilsFX.showImage(dstY, "dstY");
        CvUtilsFX.showImage(dst, "dst");

    }

}


