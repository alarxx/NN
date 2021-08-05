package utils;

import java.io.*;

public class OpenCVIO {
    //Можно сохранять 8-битные матрицы как в CvUtils

    public void SAVE() throws IOException {
        /**
         * не получалось сохранять double без погрешности, и я подумал,
         * что можно умножать на определенное 10-кратное число и не обращать внимание на число после запятой,
         * а потом просто снова делим на это 10-кратное число и получать double. Прикольно
         */
        File file = new File("c:\\Java\\test.mtcv");
        InputStream is = null;
        DataInputStream dis = null;
        FileOutputStream fos = null;
        DataOutputStream dos = null;

        double v = 0.100000006008011d; //15 цифр после запятой
        double scale = 1000000000000000d;
        long l = (long) (v * scale);


        long[] lbuf = {l};

        try {
            fos = new FileOutputStream(file);
            dos = new DataOutputStream(fos);
            for(long j: lbuf) {
                dos.writeLong(j);
            }
            dos.flush();

            is = new FileInputStream(file);
            dis = new DataInputStream(is);
            while(dis.available()>0) {
                long k = dis.readLong();
                System.out.print(k/scale + " ");
            }

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(is!=null) is.close();
            if(dis!=null) dis.close();
            if(fos!=null) fos.close();
            if(dos!=null) dos.close();
        }
    }

}
