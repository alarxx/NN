package MyNN;

public class LinearAlgebra {

    public static void multiply_mmv(float[][] m, float[] v_src, float[] v_dst){ //r*n * n*c = r*c
        //В случае ошибки размерностей матриц
        if(m[0].length!=v_src.length || m.length!= v_dst.length){ System.out.println("ERROR: in LinearAlgebra.multiply_mmv()"); return; }

        for(int i=0; i<m.length; i++){ //row
            float sum = 0;
            for(int j=0; j<m[0].length; j++){
                sum += m[i][j]*v_src[j];
            }
            v_dst[i] = sum;
        }
    }

    public static void addition_vav(float[] vto, float[] v){
        for(int i=0; i<v.length; i++){
            vto[i] += v[i];
        }
    }
}
