package utils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class LinearAlgebraUtils {
    public static void array2DToMat(double[][] arr2d, Mat mat){
        int rows = arr2d.length;
        int cols = arr2d[0].length;
        double[] arr = new double[cols*rows];
        for(int row=0, k=0; row<rows; row++)
            for(int col=0; col<cols; col++, k++)
                arr[k] = arr2d[row][col];
        mat.put(0, 0, arr);
    }


    public static double[][] getArray2DFromMat(Mat matFC1){
        double[][] arr2d = new double[matFC1.rows()][matFC1.cols()];
        double[] arr = getArrayFromMat(matFC1);

        for(int row=0, k = 0; row<matFC1.rows(); row++)
            for(int col=0; col<matFC1.cols(); col++, k++)
                arr2d[row][col] = arr[k];

        return arr2d;
    }


    public static void cleanMat(double[][] arr, int to){
        for(int row=0; row<arr.length; row++)
            for(int col=0; col<arr[0].length; col++)
                arr[row][col] = to;
    }

    public static Mat getMatFrom2DArray(double[][] arr2d){
        int rows = arr2d.length;
        int cols = arr2d[0].length;
        Mat mat = new Mat(rows, cols,  CvType.CV_64FC1);
        double[] arr1 = new double[cols*rows];
        for(int row = 0, k = 0; row<rows; row++ )
            for(int col = 0; col<cols; col++, k++)
                arr1[k] = arr2d[row][col];
        mat.put(0, 0, arr1);
        return mat;
    }

    public static double[] getArrayFromMat(Mat m){
        double[] arr = new double[m.cols()*m.rows()];
        m.get(0, 0, arr);
        return arr;
    }

    public static String[] getStringArrayFromArray2D(double[][] mat){
        String[] strArray = new String[mat.length];
        String s;
        //String - row
        for(int row=0; row<mat.length; row++){
            for(int col=0; col<mat[0].length; col++){
                //Преобразовать каждую row в строчку String
                s = "" + mat[row][col];
                if(col==0){
                    strArray[row]="";
                    strArray[row]=strArray[row].concat(s);
                }else
                    strArray[row]=strArray[row].concat(" "+s);
            }
            strArray[row] = strArray[row].concat("\n");
        }
        return strArray;
    }
}
