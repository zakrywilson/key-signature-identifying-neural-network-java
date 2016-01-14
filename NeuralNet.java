import java.util.Arrays;
import java.util.Random;

/**
 * Neural network that can identify the key signature of a arbitrary melody.
 * @author zakrywilson
 * @since 09/03/15
 */
class NeuralNet {

  /** Singleton instance */
  private static NeuralNet instance = null;

  /** Random instance */
  private Random random = null;

  private double[][] weights;
  private double[] values;
  private double[] thresholds;

  // Constants
  private static final double E = 2.71828;
  private static final double LEARNING_RATE = 0.18;

  // Optional network configurations with default values
  private static int maxIterations = 10000000; // 10 million iterations
  private static int inputNodes = 12;
  private static int hiddenNodes = 12;
  private static int outputNodes = 12;
  private static int totalNumberOfNodes = inputNodes + hiddenNodes + outputNodes;
  private static int arraySize = totalNumberOfNodes;

  // Improves readability for result array
  static final int GUESS = 0;
  static final int ERROR = 1;


  /**
   * Constructor.
   */
  private NeuralNet() {
    random = new Random(System.currentTimeMillis() * System.currentTimeMillis());
  }

  /**
   * Gets a random number.
   * @return random integer
   */
  private int rand() {
    return this.random.nextInt(Integer.MAX_VALUE);
  }

  /**
   * This function lets us set random default values for the network to iterate on.
   * @param weights
   * @param thresholds
   */
  private void connectNodes(double weights[][], double thresholds[]) {
    for (int x = 0; x < totalNumberOfNodes; x++) {
      thresholds[x] = rand() / (double) rand();
      for (int y = 0; y < totalNumberOfNodes; y++) {
        weights[x][y] = (rand() % 200) / 100.0;
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
    for (int o = inputNodes + hiddenNodes; o < totalNumberOfNodes; o++) {
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
   * @param expectedAnswer
   * @return <code>result</code> array where index 0 contains the
   *         output and index 1 contains the sum of squared errors
   */
  private double updateWeightsAndGetResults(double[] expectedAnswer) {

    double error = 0.0;

    for (int o = inputNodes + hiddenNodes; o < totalNumberOfNodes; o++) {

      double absoluteError = expectedAnswer[o - inputNodes - hiddenNodes] - values[o];
      error += (Math.pow(absoluteError, 2)) / inputNodes;
      double outputErrorGradient = values[o] * (1.0 - values[o]) * absoluteError;

      for (int h = inputNodes; h < inputNodes + hiddenNodes; h++) {
        double delta = LEARNING_RATE * values[h] * outputErrorGradient;
        weights[h][o] += delta;
        double hiddenErrorGradient = values[h] * (1 - values[h]) * outputErrorGradient * weights[h][o];
        for (int i = 0; i < inputNodes; i++) {
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

  /**
   * Gets the number of input nodes in the network.
   * @return number of input nodes
   */
  static int sizeOfInputLayer() {
    return inputNodes;
  }

  /**
   * Gets the number of hidden nodes in the network.
   * @return number of hidden nodes
   */
  static int sizeOfHiddenLayer() {
    return hiddenNodes;
  }

  /**
   * Gets the number of output nodes in the network.
   * @return number of output nodes
   */
  static int sizeOfOutputLayer() {
    return outputNodes;
  }

  /**
   * Gets the total number of nodes in the network.
   * @return total number of nodes in network
   */
  static int sizeOfNetwork() {
    return totalNumberOfNodes;
  }

  /**
   * Sets the number of nodes for each layer in the neural net.
   * @param NumberOfInputNodes - number of input nodes
   * @param NumberOfHiddenNodes - number of hidden nodes
   * @param NumberOfOutputNodes - number of output nodes
   */
  void configure(final int NumberOfInputNodes, final int NumberOfHiddenNodes, final int NumberOfOutputNodes) {
    inputNodes = NumberOfInputNodes;
    hiddenNodes = NumberOfHiddenNodes;
    inputNodes = NumberOfOutputNodes;
  }

  /**
   * Set number of iterations.
   * @param iterations - max number of iterations
   */
  void setIterations(final int iterations) {
    maxIterations = iterations;
  }

  /**
   * Singleton.
   * @return instance of NeuralNet
   */
  static NeuralNet getInstance() {
    return (instance == null) ? new NeuralNet() : instance;
  }

  /**
   * Runs the neural network through it's number of iterations.
   */
  void run() {

    weights = new double[arraySize][arraySize];
    thresholds = new double[arraySize];

    connectNodes(weights, thresholds);

    for (int iteration = 0; iteration < maxIterations; iteration++) {

      // get and set new test data
      Song song = new Song();
      final double[] frequencies = song.getFrequencies();
      double[] results = new double[2];
      final double correctAnswer = song.getKeyOfSong();

      values = Arrays.copyOf(frequencies, totalNumberOfNodes);

      // run training for the network
      activateNetwork();

      // save error and net's guess: [0]: guess & [1]: error
      double[] expectedAnswers = NetsPostprocessing.computeExpectedAnswer(correctAnswer);
      results[ERROR] = updateWeightsAndGetResults(expectedAnswers);

      // determine the net's guess and display results
      results[GUESS] = NetsPostprocessing.interpretResults(values);
      NetsPostprocessing.displayNetwork(correctAnswer, results);
    }
  }
}