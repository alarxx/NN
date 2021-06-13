package MyNN;

public class Main {
    public static void main(String[] args){
        float[][] m = new float[][]{{1, 2, 3}, {2, 2, 3}};
        float v_src[] = new float[]{1, 1, 1};
        float v_dst[] = new float[2];
        LinearAlgebra.multiply_mmv(m, v_src, v_dst);
        System.out.println(v_dst[0]);
        System.out.println(v_dst[1]);
    }
}
