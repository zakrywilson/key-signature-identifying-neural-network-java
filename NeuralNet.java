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
  private int inputNodes;
  private int hiddenNodes;
  private int outputNodes;
  private int totalNodes;


  /**
   * Constructor.
   */
  NeuralNet(final int sizeOfInputLayer, final int sizeOfHiddenLayer, final int sizeOfOutputLayer) {
    inputNodes = sizeOfInputLayer;
    hiddenNodes = sizeOfHiddenLayer;
    outputNodes = sizeOfOutputLayer;
    init();
  }

  /**
   * Runs the neural network through it's number of iterations.
   */
  NeuralNetOutput train(Song song) {

    final double correctAnswer = song.getKeyOfSong();
    values = Arrays.copyOf(song.getFrequencies(), totalNodes);

    // run training for the network
    activateNetwork();

    double error = updateWeightsAndGetResults(Processor.getExpectedAnswer(correctAnswer));
    double guess = interpretResults();
    return new NeuralNetOutput(guess, correctAnswer, error);
}

  /**
   * Initializes neural network characteristics.
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