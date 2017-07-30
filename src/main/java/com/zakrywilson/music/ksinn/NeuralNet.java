package com.zakrywilson.music.ksinn;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 * Neural network that can identify the key signature of a arbitrary melody.
 *
 * @author Zach Wilson
 * @since 09/03/15
 */
class NeuralNet implements Serializable {

    private static final long serialVersionUID = 7919609507025014243L;

    /**
     * The total number of input nodes in the neural network
     */
    private int inputNodes;

    /**
     * The total number of hidden nodes in the neural network
     */
    private int hiddenNodes;

    /**
     * The total number of output nodes in the neural network
     */
    private int outputNodes;

    /**
     * The total number of nodes in the neural network
     */
    private int totalNodes;

    /**
     * The neural network's learning rate
     */
    private double learningRate;

    /**
     * The values array used for containing all nodes in neural network
     */
    private double[] values;

    /**
     * The thresholds of the neural network's nodes
     */
    private double[] thresholds;

    /**
     * The weights of the neural network's nodes
     */
    private double[][] weights;

    /**
     * Random number generator
     */
    private static Random random;

    /**
     * Constant mathematical value e
     */
    private static final double E = Math.E;

    /**
     * Constructor.
     *
     * @param inputNodes - number of nodes in input layer
     * @param hiddenNodes - number of nodes in hidden layer
     * @param outputNodes - number of nodes in output layer
     */
    NeuralNet(final short inputNodes, final short hiddenNodes, final short outputNodes, final double learningRate) {
        this.inputNodes = inputNodes;
        this.hiddenNodes = hiddenNodes;
        this.outputNodes = outputNodes;
        this.learningRate = learningRate;
        init();
    }

    /**
     * Runs through one iteration.
     *
     * @param song - a new song to train on
     * @return output - the output containing all relevant results
     */
    NeuralNetOutput run(final Song song) {

        values = Arrays.copyOf(song.getFrequencies(), totalNodes);

        // run training for the network
        activateNetwork();

        double error = updateWeights(song.getExpectedOutput());
        double guess = interpretResults();
        return new NeuralNetOutput(guess, error);
    }

    /**
     * Initializes neural network characteristics: weights, thresholds, etc.
     */
    private void init() {
        totalNodes = inputNodes + hiddenNodes + outputNodes;
        random = new Random(System.currentTimeMillis() * System.currentTimeMillis());
        weights = new double[totalNodes][totalNodes];
        thresholds = new double[totalNodes];
        connectNodes();
    }

    /**
     * Sets random default values for the network to iterate over.
     */
    private void connectNodes() {
        for (int x = 0; x < totalNodes; x++) {
            thresholds[x] = getRandom() / (double) getRandom();
            for (int y = 0; y < totalNodes; y++) {
                weights[x][y] = (getRandom() % 200) / 100.0;
            }
        }
    }

    /**
     * Activates the neural network.
     */
    private void activateNetwork() {

        // For every hidden node
        for (int h = inputNodes; h < inputNodes + hiddenNodes; h++) {
            double weightedInput = 0.0;

            // Add up the weighted inputs
            for (int i = 0; i < inputNodes; i++) {
                weightedInput += weights[i][h] * values[i];
            }

            // Handle the thresholds
            weightedInput += (-1 * thresholds[h]);
            values[h] = 1.0 / (1.0 + Math.pow(E, -weightedInput));
        }

        // For every output node
        for (int o = inputNodes + hiddenNodes; o < totalNodes; o++) {
            double weightedInput = 0.0;

            // Add up the weighted inputs
            for (int h = inputNodes; h < inputNodes + hiddenNodes; h++) {
                weightedInput += weights[h][o] * values[h];
            }
            weightedInput += (-1 * thresholds[o]);
            values[o] = 1.0 / (1.0 + Math.pow(E, -weightedInput));
        }
    }

    /**
     * Update weights and get results.
     *
     * @param expectedResults - the correct answer for the neural net's output nodes
     * @return results array (where index 0 contains output and index 1 contains sum of squared
     * errors)
     */
    private double updateWeights(final double[] expectedResults) {

        double error = 0.0;

        for (int o = inputNodes + hiddenNodes; o < totalNodes; o++) {

            double absoluteError = expectedResults[o - inputNodes - hiddenNodes] - values[o];
            error += (Math.pow(absoluteError, 2)) / inputNodes;
            double outputErrorGradient = values[o] * (1.0 - values[o]) * absoluteError;

            for (int h = inputNodes; h < inputNodes + hiddenNodes; h++) {
                double delta = learningRate * values[h] * outputErrorGradient;
                weights[h][o] += delta;
                double hiddenErrorGradient = values[h] * (1 - values[h]) * outputErrorGradient * weights[h][o];
                for (int i = 0; i < inputNodes; i++) {
                    double _delta = learningRate * values[i] * hiddenErrorGradient;
                    weights[i][h] += _delta;
                }

                double thresholdData = learningRate * -1 * hiddenErrorGradient;
                thresholds[h] += thresholdData;
            }
            double delta = learningRate * -1 * outputErrorGradient;
            thresholds[o] += delta;
        }
        return error;
    }

    /**
     * Finds the highest ranked note in values array and returns that note.
     *
     * @return neural network's guess
     */
    private double interpretResults() {
        double[] output = Arrays.copyOfRange(values, inputNodes + hiddenNodes,
                                             values.length);
        int maxValueIndex = 0;
        for (int i = 1; i < output.length; i++) {
            double newValue = output[i];
            if ((newValue > output[maxValueIndex])) {
                maxValueIndex = i;
            }
        }
        return maxValueIndex;
    }

    /**
     * Gets a random number.
     *
     * @return random integer
     */
    private int getRandom() {
        return random.nextInt(Integer.MAX_VALUE);
    }

    @Override
    public String toString() {
        String string = "Neural network attributes:\n";
        string += "\tInput nodes: " + this.inputNodes + "\n";
        string += "\tHidden nodes: " + this.hiddenNodes + "\n";
        string += "\tOutput nodes: " + this.outputNodes + "\n";
        string += "\tLearning rate: " + this.learningRate + "\n";
        string += "--------------------------------------------------------------";
        return string;
    }
}