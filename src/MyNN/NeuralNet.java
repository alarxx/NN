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

    public float[] feedforward(float[] inputs){
        layers[0].neurons = Arrays.copyOf(inputs, inputs.length);
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
            }
        }
        return layers[n - 1].neurons;
    }

    public void backpropagation(){
        /**
         * w = w - dw
         * dw = lr * E'
         * E = (t-o)^2
         * E' = -2(t-o)*o'(w), где 2-йка всего лишь масштабирует
         * o' = (1/(1+e^y(w)))' = o*(1-o) * y'(w) = o*(1-o) * i, где y(w) = sum(i*w)
         *
         * E_h(w)' = -2(t-o)) * o'(f(w), g(w))
         *
         * Есть проблема затухающего и взрывного градиента (надо проверить)
         */
    }

    public float sigmoid(float x){
        return 1f/(1f+(float)Math.exp(x));
    }
    public float derivative_sigmoid(float o){
        return o*(1-o);
    }
}
