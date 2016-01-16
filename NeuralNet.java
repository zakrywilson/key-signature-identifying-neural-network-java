import java.util.Arrays;
import java.util.Random;

/**
 * Neural network that can identify the key signature of a arbitrary melody.
 * @author zakrywilson
 * @since 09/03/15
 */
class NeuralNet {

  // Singleton instance
  private static NeuralNet instance = null;

  // Aspects of the neural network nodes
  private double[][] weights;
  private double[] values;
  private double[] thresholds;

  // Random instance
  private Random random;

  // Constants
  private static final double E = 2.71828;
  private static final double LEARNING_RATE = 0.18;

  // Optional network configurations with default values
  private static int maxIterations;
  private static int inputNodes;
  private static int hiddenNodes;
  private static int outputNodes;
  private static int totalNodes;


  /**
   * Constructor.
   */
  private NeuralNet() {
    random = new Random(System.currentTimeMillis() * System.currentTimeMillis());
  }

  /**
   * Singleton.
   * @return instance of NeuralNet
   */
  static NeuralNet getInstance() {
    return (instance == null) ? new NeuralNet() : instance;
  }

  /**
   * Sets the number of nodes for each layer in the neural net.
   * @param numberOfInputNodes - number of input nodes
   * @param numberOfHiddenNodes - number of hidden nodes
   * @param numberOfOutputNodes - number of output nodes
   */
  void configure(final int numberOfInputNodes,
                 final int numberOfHiddenNodes,
                 final int numberOfOutputNodes) {
    inputNodes = numberOfInputNodes;
    hiddenNodes = numberOfHiddenNodes;
    outputNodes = numberOfOutputNodes;
    totalNodes = inputNodes + hiddenNodes + outputNodes;
  }

  /**
   * Set number of iterations.
   * @param iterations - max number of iterations
   */
  void setIterations(final int iterations) {
    maxIterations = iterations;
  }

  /**
   * Runs the neural network through it's number of iterations.
   */
  void run() {

    weights = new double[totalNodes][totalNodes];
    thresholds = new double[totalNodes];

    connectNodes();

    for (int iteration = 0; iteration < maxIterations; iteration++) {

      // get and set new test data
      Song song = new Song();
      final double correctAnswer = song.getKeyOfSong();
      values = Arrays.copyOf(song.getFrequencies(), totalNodes);

      // run training for the network
      activateNetwork();

      double error = updateWeightsAndGetResults(PostProcessor.getExpectedAnswer(correctAnswer));
      double guess = PostProcessor.interpretResults(values);
      PostProcessor.displayResults(correctAnswer, guess, error);
    }
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
    return inputNodes + hiddenNodes + outputNodes;
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
   */
  private void connectNodes() {
    for (int x = 0; x < totalNodes; x++) {
      thresholds[x] = rand() / (double) rand();
      for (int y = 0; y < totalNodes; y++) {
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
   * @param expectedAnswer - the correct answer
   * @return <code>result</code> array where index 0 contains the
   *         output and index 1 contains the sum of squared errors
   */
  private double updateWeightsAndGetResults(double[] expectedAnswer) {

    double error = 0.0;

    for (int o = inputNodes + hiddenNodes; o < totalNodes; o++) {

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
}