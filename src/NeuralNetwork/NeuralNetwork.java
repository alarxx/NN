package NeuralNetwork;

import java.util.Arrays;

public class NeuralNetwork {

    private double learningRate;
    private Layer[] layers;

    public NeuralNetwork(double learningRate, int... sizes) {
        this.learningRate = learningRate;
        int l = sizes.length; //количество слоев

        layers = new Layer[l];

        for (int i = 0, nextSize = 0; i < l; i++, nextSize=0) {
            if(i < l - 1) //Если это не последний слой, макс число в итерации = sumOf_Length-1
                nextSize = sizes[i + 1]; //Размер кол-ва нейронов после этого слоя
            layers[i] = new Layer(sizes[i], nextSize);
            for (int j = 0; j < sizes[i]; j++) {
                layers[i].biases[j] = Math.random() * 2.0 - 1.0; //Случайный вес для байеса на этом слое
                for (int k = 0; k < nextSize; k++) {
                    layers[i].weights[j][k] = Math.random() * 2.0 - 1.0; //Случайные веса для каждого нейрона в слое
                }
            }
        }
    }

    public NeuralNetwork(double learningRate, int[] sizes, boolean do_not_load) {
        this.learningRate = learningRate;
        int l = sizes.length;
        layers = new Layer[l];
        for (int i = 0; i < l; i++) {
            int nextSize = 0;
            if(i < l - 1) nextSize = sizes[i + 1];
            layers[i] = new Layer(sizes[i], nextSize);
        }
    }

    public double[] feedForward(double[] inputs) {
        layers[0].neurons = Arrays.copyOf(inputs, inputs.length);
        for (int i = 1; i < layers.length; i++)  {
            Layer l = layers[i - 1];
            Layer l1 = layers[i];
            for (int j = 0; j < l1.size; j++) {

                l1.neurons[j] = 0;
                for (int k = 0; k < l.size; k++) //Суммируем просто все inputs на weights
                    l1.neurons[j] += l.neurons[k] * l.weights[k][j];

                l1.neurons[j] += l1.biases[j];//Не забываем про байес
                l1.neurons[j] = activation(l1.neurons[j]); //Проводим через функцию активации
            }
        }
        return layers[layers.length - 1].neurons;
    }

    public void backpropagation(double[] targets) {
        double[] errors = new double[layers[layers.length - 1].size];
        for (int i = 0; i < layers[layers.length - 1].size; i++) {
            errors[i] = targets[i] - layers[layers.length - 1].neurons[i];
        }
        for (int k = layers.length - 2; k >= 0; k--) {
            Layer l = layers[k];
            Layer l1 = layers[k + 1];
            double[] errorsNext = new double[l.size];
            double[] gradients = new double[l1.size];
            for (int i = 0; i < l1.size; i++) {
                gradients[i] = errors[i] * derivative(layers[k + 1].neurons[i]);
                gradients[i] *= learningRate;
            }
            double[][] deltas = new double[l1.size][l.size];
            for (int i = 0; i < l1.size; i++) {
                for (int j = 0; j < l.size; j++) {
                    deltas[i][j] = gradients[i] * l.neurons[j];
                }
            }
            for (int i = 0; i < l.size; i++) {
                errorsNext[i] = 0;
                for (int j = 0; j < l1.size; j++) {
                    errorsNext[i] += l.weights[i][j] * errors[j];
                }
            }
            errors = new double[l.size];
            System.arraycopy(errorsNext, 0, errors, 0, l.size);
            double[][] weightsNew = new double[l.weights.length][l.weights[0].length];
            for (int i = 0; i < l1.size; i++) {
                for (int j = 0; j < l.size; j++) {
                    weightsNew[j][i] = l.weights[j][i] + deltas[i][j];
                }
            }
            l.weights = weightsNew;
            for (int i = 0; i < l1.size; i++) {
                l1.biases[i] += gradients[i];
            }
        }
    }

    private double activation(double x){
        return 1 / (1 + Math.exp(-x));
    }
    private double derivative(double y){
        return y * (1 - y);
    }

    public Layer[] getLayers(){ return layers; }
}