package NeuralNetwork;

import java.io.*;

//Имеет 2 основных метода: 1) Сохранение нейронки
//                         2) Загрузка нейронки

public class Loader {

    private static String config = "\\config", extension = ".mtcv";

    public static boolean saveNeuralNet(NeuralNetwork nn, String path){
        if(path.isEmpty() || nn == null) return false;
        try {
            Layer[] layers = nn.getLayers();

            int l = layers.length;
            double[][] sizes = new double[1][l];

            for (int i = 0; i < l - 1; i++) { //На последнем слое нет весов, последний слой - ответ(prediction)
                double[][] arr = layers[i].weights;
                saveArray2D(path + "\\" + i + extension, arr);

                sizes[0][i] = layers[i].neurons.length;
            }

            sizes[0][l-1] = layers[l-1].neurons.length;
            saveArray2D(path + config, sizes);

        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static NeuralNetwork loadNeuralNetwork(String path, double lr){
        File mainFile = new File(path);
        if(!mainFile.exists())
            return new NeuralNetwork(0, 0);

        int sumOf_Layers = mainFile.listFiles().length; //Там файлы с весами и одна с конфигом

        int[] sizes = new int[sumOf_Layers];
        double[][] configArr2D = readArray2DFromFile(mainFile.getAbsolutePath() + config);
        for(int i=0; i<sumOf_Layers; i++)
            sizes[i] = (int) configArr2D[0][i];

        NeuralNetwork resNN = new NeuralNetwork(lr, sizes, true);
        Layer[] layers = resNN.getLayers();

        for(int i=0; i<sumOf_Layers-1; i++) {
            String weigthsF = mainFile.getAbsolutePath() + "\\" + i + extension;
            layers[i].weights = readArray2DFromFile(weigthsF);
        }

        return resNN;
    }

    private static boolean saveArray2D(String pathNewFile, double[][] arr2d){
        String[] rows = getStringArrayFromArray2D(arr2d);
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
    private static String[] getStringArrayFromArray2D(double[][] mat){
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

    private static double[][] readArray2DFromFile(String path){
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
}
