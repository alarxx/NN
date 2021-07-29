package utils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.*;

public class OpenCVIO {
    public static boolean saveArray2D(String pathNewFile, double[][] arr2d){
        String[] strArray = LinearAlgebraUtils.getStringArrayFromArray2D(arr2d);
        return saveStringArrayToNewFile(pathNewFile, strArray);
    }

    //не пользоваться утечка памяти
    public static Mat readRedactedMats(String path){//Нужен полный путь вместе с название и расширением файла
        Mat m = LinearAlgebraUtils.getMatFrom2DArray(OpenCVIO.readArray2DFromFile(path));
        return m;
    }

    //Нужен полный путь вместе с название и расширением файла
    //Матрицу он переопределяет
    public static void setRedactedMats(String path, Mat mat){
        try{
            FileInputStream fileInputStream = new FileInputStream(path);
            BufferedInputStream buf = new BufferedInputStream(fileInputStream);

            char[] cs = new char[buf.available()];
            for(int i=0; i<cs.length; i++){
                cs[i]=(char)buf.read();
            }
            String m = new String(cs);

            String[] s = m.split("\n"); //rows
            String[] d = s[0].split(" "); //чтобы узнать сколько cols, больше не используется

            double[] arr = new double[s.length * d.length];
            for(int col=0; col<d.length; col++)
                arr[col] = Double.parseDouble(d[col]);
            for(int row=1; row<s.length; d = s[row].split(" "), row++)
                for(int col=0; col<d.length; col++)
                    arr[col] = Double.parseDouble(d[col]);

            mat.create(s.length, d.length, CvType.CV_64FC1);
            mat.put(0, 0, arr);
            fileInputStream.close();
            buf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //Дополнительные методы

    public static double[][] readArray2DFromFile(String path){
        double[][] arr2d = null;
        try{
            FileInputStream fileInputStream = new FileInputStream(path);
            BufferedInputStream buf = new BufferedInputStream(fileInputStream);

            char[] cs = new char[buf.available()];
            for(int i=0; i<cs.length; i++){
                cs[i]=(char)buf.read();
            }
            String m = new String(cs);

            String[] s = m.split("\n"); //rows
            String[] d = s[0].split(" "); //чтобы узнать сколько cols, больше не используется

            arr2d = new double[s.length][d.length];

            for(int col=0; col<d.length; col++)
                arr2d[0][col] = Double.parseDouble(d[col]);

            for(int row=1; row<s.length; row++){
                d = s[row].split(" ");
                for(int col=0; col<d.length; col++)
                    arr2d[row][col] = Double.parseDouble(d[col]);
            }

            fileInputStream.close();
            buf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return arr2d;
    }



    public static boolean saveStringArrayToNewFile(String pathNewFile, String[] rows){
        if(rows.length==0) return false;
        try(BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(pathNewFile))){
            for(int i=0; i<rows.length; i++){
                byte[] bs = rows[i].getBytes();
                bufferedOutputStream.write(bs, 0, bs.length);
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void redactPhotos(String pathFrom, String pathTo, Size size, String addSymb){
        File dir = new File(pathFrom); //path указывает на директорию
        File[] arrFiles = dir.listFiles();
        //List<File> lst = Arrays.asList(arrFiles);
        Mat imgOriginal = new Mat();
        Mat imgRedact = new Mat();//Черно-белые контуры
        Mat imgSave = new Mat();//По идее Input Mat
        double[][] mat2d;
        if(arrFiles != null) {
            for (int i = 0; i < arrFiles.length; i++) {
                System.out.println(arrFiles[i]);

                imgOriginal = Imgcodecs.imread("" + arrFiles[i]);
                //CvUtilsFX.showImage(imgOriginal, "mat number " + i);

                Imgproc.cvtColor(imgOriginal, imgRedact, Imgproc.COLOR_BGR2GRAY);
                Imgproc.resize(imgRedact, imgRedact, size);

                //Или лучше threshold в общем находим границы. В черно-белое.
                Imgproc.Canny(imgRedact, imgRedact, 80, 200);

                //8CU в 64FC ([0;255] -> [0.0;1.0])
                imgRedact.convertTo(imgRedact, CvType.CV_64FC1, 1.0d/255.0d);

                //Mat в Mat с единственным col
                imgSave = imgRedact.reshape(0, imgRedact.rows() * imgRedact.cols());

                mat2d = LinearAlgebraUtils.getArray2DFromMat(imgSave);

                //сохраняем
                if (OpenCVIO.saveArray2D(pathTo +"\\"+ addSymb + i + ".mtcv", mat2d)) {
                    //Imgcodecs.imwrite(pathTo+"Kimages\\"+ i + ".png", imgRedact);
                    System.out.println("saved to: " + pathTo +"\\"+ addSymb + i + ".mtcv");
                } else {
                    System.out.println("error with: " + pathTo +"\\"+ addSymb + i + ".mtcv");
                }
            }
        }else {
            System.out.println("error! folder was not found");
        }
        imgOriginal.release();
        imgRedact.release();
        imgSave.release();
    }


}
