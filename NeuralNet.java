import java.util.Arrays;
import java.util.Random;

/**
 * Neural network that can identify the key signature of a arbitrary melody.
 * @author zakrywilson
 * @since 09/03/15
 */
class NeuralNet {

  // Aspects of the neural network nodes
  private int inputNodes;
  private int hiddenNodes;
  private int outputNodes;
  private int totalNodes;
  private double learningRate;
  private double[] values;
  private double[] thresholds;
  private double[][] weights;

  // Random instance
  private Random random;

  // Constants
  private static final double E = 2.71828;


  /**
   * Constructor.
   * @param inputNodes - number of nodes in input layer
   * @param hiddenNodes - number of nodes in hidden layer
   * @param outputNodes - number of nodes in output layer
   */
  NeuralNet(final int inputNodes, final int hiddenNodes, final int outputNodes, final double learningRate) {
    this.inputNodes = inputNodes;
    this.hiddenNodes = hiddenNodes;
    this.outputNodes = outputNodes;
    this.learningRate = learningRate;
    init();
  }

  /**
   * Runs through one iteration.
   * @param song - a new song to train on
   * @return output - the output containing all relevant results
   */
  NeuralNetOutput run(final Song song) {

    final double correctAnswer = song.getKeyOfSong();
    values = Arrays.copyOf(song.getFrequencies(), totalNodes);

    // run training for the network
    activateNetwork();

    double error = updateWeights(song.getExpectedOutput());
    double guess = interpretResults();
    return new NeuralNetOutput(guess, correctAnswer, error);
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
   * Gets a random number.
   * @return random integer
   */
  private int getRandom() {
    return this.random.nextInt(Integer.MAX_VALUE);
  }

  /**
   * This function lets us set random default values for the network to iterate on.
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
   * @param expectedResults - the correct answer for the neural net's output nodes
   * @return <code>result</code> array where index 0 contains the
   *         output and index 1 contains the sum of squared errors
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
   * Finds the highest scored note in <code>values[]</code> and returns that note.
   * @return net's guess
   */
  private double interpretResults() {
    int index = totalNodes - outputNodes + 1;
    double offset = index - 1;
    double guess = values[index - 1];
    double note = index - 1.0;
    double val;

    while (index < values.length) {
      val = values[index];
      if (val > guess) {
        guess = val;
        note = index;
      }
      index++;
    }
    return note - offset;
  }
}