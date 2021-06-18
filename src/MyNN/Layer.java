package MyNN;

public class Layer {
    int size;
    float neurons[]; //на первой слое - I(const), на скрытых - просто значение после функции активации sigm(W), на выходном - O(W) или Y^(W)
    float W[][]; //веса между этим слоем и следующим
    float B[]; //веса байес нейрона(1) со следующим слоем

    public Layer(int size, int nextSize){
        this.size = size;
        neurons = new float[size];
        W = new float[nextSize][size];
        B = new float[nextSize];
    }

    public void randomly_fill_w(){
        int row = W.length, col = size;
        for(int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                W[i][j] = rand_min1to1();
            }
        }
    }
    public void randomly_fill_b(){
        for(int i=0; i<B.length; i++){
            B[i] = rand_min1to1();
        }
    }

    //Возвращает значение [-1;1)
    public float rand_min1to1(){
        return (float)Math.random() * 2f - 1f;
    }
}
