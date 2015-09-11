package musicTheory;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author zakrywilson
 * @since 09/03/15
 */
public class MusicTheoryNets {

    // Constants
    protected static final int GUESS = 0;
    protected static final int ERROR = 1;
    protected static final int NUM_INPUT_NODES = 12;
    protected static final int NUM_HIDDEN_NODES = 24;
    protected static final int NUM_OUTPUT_NODES = 12;
    protected static final int NUM_NODES = NUM_INPUT_NODES + NUM_HIDDEN_NODES + NUM_OUTPUT_NODES;
    protected static final int ARRAY_SIZE = NUM_NODES;
    protected static final int MAX_ITERATIONS = 10000000; // 10,000,000
    protected static final double E = 2.71828;
    protected static final double LEARNING_RATE = 0.18;
    
    // logging
//  private static final Logger logger = Logger.getAnonymousLogger();

    // globals
    private Random random = null;

    /**
     * Constructor: initializes Random object
     */
    public MusicTheoryNets() {
        this.random = new Random(System.currentTimeMillis() * System.currentTimeMillis());
    }

    /**
     * @return random integer
     */
    private int rand() {
        return this.random.nextInt(Integer.MAX_VALUE);
    }

    /**
     * This function lets us set random default values for the network to iterate on
     * @param weights
     * @param thresholds
     */
    private void connectNodes(double weights[][], double thresholds[]) {
        for (int x = 0; x < NUM_NODES; x++) {
            thresholds[x] = rand() / (double) rand();
            for (int y = 0; y < NUM_NODES; y++) {
                weights[x][y] = (rand() % 200) / 100.0;
            }
        }
    }

    /**
     * @param weights
     * @param values
     * @param thresholds
     */
    private void activateNetwork(double weights[][], double values[], double thresholds[]) {
        // For every hidden node
        for (int h = NUM_INPUT_NODES; h < NUM_INPUT_NODES + NUM_HIDDEN_NODES; h++) {
            double weightedInput = 0.0;
            
            // Add up the weighted inputs
            for (int i = 0; i < NUM_INPUT_NODES; i++) {
                weightedInput += weights[i][h] * values[i];
            }
            
            // Handle the thresholds
            weightedInput += (-1 * thresholds[h]);
            values[h] = 1.0 / (1.0 + Math.pow(E, -weightedInput));
        }

        // For every output node
        for (int o = NUM_INPUT_NODES + NUM_HIDDEN_NODES; o < NUM_NODES; o++) {
            double weightedInput = 0.0;

            // Add up the weighted inputs
            for (int h = NUM_INPUT_NODES; h < NUM_INPUT_NODES + NUM_HIDDEN_NODES; h++) {
                weightedInput += weights[h][o] * values[h];
            }
            weightedInput += (-1 * thresholds[o]);
            values[o] = 1.0 / (1.0 + Math.pow(E, -weightedInput));
        }
    }

    /**
     * @param weights
     * @param values
     * @param expectedAnswer
     * @param thresholds
     * @return <code>result</code> array where index 0 contains the  
     *         output and index 1 contains the sum of squared errors
     */
    private double updateWeightsAndGetResults(double weights[][],
            double values[], double[] expectedAnswer, double thresholds[]) {

        double error = 0.0;

        for (int o = NUM_INPUT_NODES + NUM_HIDDEN_NODES; o < NUM_NODES; o++) {
            
            double absoluteError = expectedAnswer[o - NUM_INPUT_NODES - NUM_HIDDEN_NODES] - values[o];
            error += (Math.pow(absoluteError, 2)) / NUM_INPUT_NODES;
            double outputErrorGradient = values[o] * (1.0 - values[o]) * absoluteError;

            for (int h = NUM_INPUT_NODES; h < NUM_INPUT_NODES + NUM_HIDDEN_NODES; h++) {
                double delta = LEARNING_RATE * values[h] * outputErrorGradient;
                weights[h][o] += delta;
                double hiddenErrorGradient = values[h] * (1 - values[h]) * outputErrorGradient * weights[h][o];
                for (int i = 0; i < NUM_INPUT_NODES; i++) {
                    double _delta = LEARNING_RATE * values[i] * hiddenErrorGradient;
                    weights[i][h] += _delta;
                }

                double thresholdData = LEARNING_RATE * -1 * hiddenErrorGradient;
                thresholds[h] += thresholdData;
            }
            double delta = LEARNING_RATE * -1 * outputErrorGradient;
            thresholds[o] += delta;
        }
        return error;
    }
    
    
    public static void main(String[] args) {
        MusicTheoryNets net = new MusicTheoryNets();

        System.out.println("Neural Network Program\n");

        double[][] weights = new double[ARRAY_SIZE][ARRAY_SIZE];
        double[] thresholds = new double[ARRAY_SIZE];

        net.connectNodes(weights, thresholds);

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            
            // get and set new test data
            Song song = new Song();
            double[] values = new double[NUM_NODES];
            double[] frequencies = song.getFrequencies();
            double[] results = new double[2];
            double correctAnswer = song.getKeyOfSong();
            
            for (int i = 0; i < frequencies.length; i++) {
                values[i] = frequencies[i];
            }
            
            // run training for the network
            net.activateNetwork(weights, values, thresholds);
            
            // save error and net's guess: [0]: guess & [1]: error
            double[] expectedAnswers = NetsPostprocessing.computeExpectedAnswer(correctAnswer);
            results[ERROR] = net.updateWeightsAndGetResults(weights, values, expectedAnswers, thresholds);
            
            // determine the net's guess and display results
            results[GUESS] = NetsPostprocessing.interpretResults(values);
            NetsPostprocessing.displayNetwork(correctAnswer, results);
        }
    }
}
