package OpenCV;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class SobelEdge {
    public static Mat Sobel(Mat src){
        Mat dst = new Mat();

        if(src.channels()>1){
            Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
        }

        //Creating an empty matrix to store the result
        Mat dstX = new Mat();
        Mat dstY = new Mat();

        // Creating kernel matrix
        Mat kX = Mat.ones(3,3, CvType.CV_32FC1);
        Mat kY = Mat.ones(3,3, CvType.CV_32FC1);
        double[] kernelX = new double[]{-1, 0, 1, -2, 0, 2, -1, 0, 1};
        double[] kernelY = new double[]{-1, -2, -1, 0, 0, 0, 1, 2, 1};
        kX.put(0, 0, kernelX);
        kY.put(0, 0, kernelY);

        Imgproc.filter2D(src, dstX, CvType.CV_64FC1, kX);
        Imgproc.filter2D(src, dstY, CvType.CV_64FC1, kY);

        Core.pow(dstX, 2, dstX);
        Core.pow(dstY, 2, dstY);
        Core.add(dstX, dstY, dst);
        Core.sqrt(dst, dst);
        Core.divide(dst, new Scalar(1442.4978), dst, 255); //sqrt(1020^2 * 2)

        dstX.convertTo(dstX, CvType.CV_8UC1);
        dstY.convertTo(dstY, CvType.CV_8UC1);
        dst.convertTo(dst, CvType.CV_8UC1);
        return dst;
    }

}
