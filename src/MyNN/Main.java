package MyNN;

public class Main {
    public static void main(String[] args){
        new Main().run();
    }
    public void run(){
        Main m = new Main();
        NeuralNet nn = new NeuralNet(0.1f, 3, 6, 6, 2);

        for (int i=0; i<1000000; i++) {
            nn.feedforward(new float[]{1, 0, 1}, 0.5f);
            nn.backpropagation(new float[]{1, 0});
            nn.feedforward(new float[]{0, 1, 0}, 0.5f);
            nn.backpropagation(new float[]{0, 1});
        }

        System.out.println("1-st data: ");
        float[] ans = nn.feedforward(new float[]{1, 1, 1});
        printArray(ans);

        System.out.println("2-nd data: ");
        ans = nn.feedforward(new float[]{1, 1, 0});
        printArray(ans);

    }

    public void run1() {
        float f = 10.00001f;
        //Нужно сохранить это значение и потом его восстановить

    }

    public static void printArray(float[] arr){
        for(int i=0; i<arr.length; i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
    public static void printArray2(float[][] arr2){
        for(int r = 0; r < arr2.length; r++){
            for(int c=0; c<arr2[0].length; c++){
                System.out.print(arr2[r][c] + " ");
            }
            System.out.println();
        }
    }
}
