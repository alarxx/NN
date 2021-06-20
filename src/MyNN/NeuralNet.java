package MyNN;

import java.util.Arrays;

public class NeuralNet {
    private Layer[] layers;
    private final float lr;

    public NeuralNet(float lr, int...sizes){
        this.lr = lr;
        int n = sizes.length;
        layers = new Layer[n];

        for(int i=0; i<n; i++){
            int nextSize = i<n-1 ? sizes[i+1] : 0; //[0 ; n-1], (n-1) - последний слой, перед которым больше нет нейронов(весов)
            layers[i] = new Layer(sizes[i], nextSize);
            layers[i].randomly_fill_w();
            layers[i].randomly_fill_b();
        }
    }

    //Попытка добавить дропаут
    public float[] feedforward(float[] inputs){ //По-моему все идеально(не проверялось 170621)
        //float probability = 0.3f;
        //lr*=1/p;
        layers[0].neurons = Arrays.copyOf(inputs, inputs.length);
        //Вместо верхней строчки ставить цикл
        // for(int i=0; i<layers[0].size; i++){
        //      if(Math.random()<p){layers[0].neurons[i]=0;}else{layers[0].neurons[i]=inputs[i];}
        // }
        int n = layers.length;
        for(int i=1; i<n; i++) {
            Layer l = layers[i - 1];
            Layer l1 = layers[i];

            for (int j = 0; j < l.W.length; j++) { //row in W matrix
                l1.neurons[j] = 0;
                for (int k = 0; k < l.W[0].length; k++)
                    l1.neurons[j] += l.W[j][k] * l.neurons[k];
                l1.neurons[j] += l.B[j];
                l1.neurons[j] = sigmoid(l1.neurons[j]);
                //Возможно здесь надо поставить дропаут

                //if(Math.random()<p){l1.neurons[j]=0;}
            }
        }
        return layers[n - 1].neurons;
    }

    public void backpropagation(float[] target){
        /**
         * w = w - dw
         * dw = lr * E'
         * E = (t-o)^2
         * E' = -2(t-o)*o'(w), где 2-йка всего лишь масштабирует
         * o' = (1/(1+e^y(w)))' = o*(1-o) * y'(w) = o*(1-o) * i, где y(w) = sum(i*w)
         *
         * E_h(w)' = -2(t-o)) * o'( f(w), g(w) ) //Производная функции зависящей от нескольких других функций
         *
         * Есть проблема затухающего и взрывного градиента (надо проверить)
         * Я неплохо так сократил код onirgi
         */

        float[] errors = new float[layers.length];

        for(int j=0; j < layers.length; j++){
            errors[j] = target[j] - layers[layers.length-1].neurons[j]; //E' = -2(t-o) * o(1-o) -> w+=lr*E'*do
        }

        for(int i = layers.length-2; i>=0; i--){
            Layer l = layers[i];
            Layer l1 = layers[i+1];

            float[] errorsNext = new float[l.size];
            float[] gradients = new float[l1.size];

            for(int j=0; j < l1.size; j++){
                gradients[j] = lr * errors[j] * derivative_sigmoid(l1.neurons[j]);
                for(int k=0; k<l.size; k++){
                    errorsNext[k] += errors[j] * l.W[j][k];
                    l.W[j][k] += gradients[j] * l.neurons[k]; //там минус на минус дал плюс
                }
            }

            errors = new float[l.size];
            System.arraycopy(errorsNext, 0, errors, 0, l.size);

            for (int j = 0; j < l1.size; j++)
                l.B[j] += gradients[j];
        }

    }

    public float sigmoid(float x){
        return 1f/(1f+(float)Math.exp(-x));
    }
    public float derivative_sigmoid(float o){
        return o*(1-o);
    }

    public void print_W_onLayer(int i){
        printArray2(layers[i].W);
    }

    public void printArray2(float[][] arr2){
        for(int r = 0; r < arr2.length; r++){
            for(int c=0; c<arr2[0].length; c++)
                System.out.print(arr2[r][c] + " ");
            System.out.println();
        }
    }


    public float[] feedforward_linearAlgebra(float[] inputs){
        /**
         * В цикле от [0; n-1], где n - кол-во слоев
         * 1)X=WI; X+B
         * 2)y^=1/(1+e^(-X));
         *
         * Есть куча лишних итераций, которых можно избежать объединив некоторые циклы.
         */
        layers[0].neurons = Arrays.copyOf(inputs, inputs.length);
        int n = layers.length;
        for(int i=1; i<n; i++){
            Layer l = layers[i-1];
            Layer l1 = layers[i];
            LinearAlgebra.multiply_mmv(l.W, l.neurons, l1.neurons);
            LinearAlgebra.addition_vav(l1.neurons, l.B);
            for(int j=0; j<l1.size; j++)
                l1.neurons[j] = sigmoid(l1.neurons[j]);
        }
        return layers[n - 1].neurons;
    }
}
