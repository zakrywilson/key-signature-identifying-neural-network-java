/**
 * Runs the training of the neural network.
 * @author zakrywilson
 * @since 01/14/16
 */
class Manager {

  private static boolean verbose = false;
  private static boolean help = false;

  /**
   * Main: runs program by training neural network over a specified iterations.
   * @param args - args not used
   */
  public static void main(String[] args) {
    int resetRate = 10000;
    int maxIterations = 10000000;
    NeuralNet net = new NeuralNet(12, 12, 12, 0.18);

    validateCommandlineArguments(args);
    if (help) {
      displayHelp();
    } else if (verbose) {
      verboseTest(net, maxIterations);
    } else {
      normalTest(net, maxIterations, resetRate);
    }
  }

  /**
   * Tests neural network with verbose output.
   * @param net - neural network
   * @param maxIt - specified training iterations
   */
  private static void verboseTest(NeuralNet net, int maxIt) {
    for (int iterations = 0; iterations < maxIt; ++iterations) {
      Song song = new Song();
      NeuralNetOutput output = net.run(song);
      output.setCorrectAnswer(song.getKeyOfSong());
      displayVerbose(output);
    }
    System.out.println("\nFinal test...");
    for (int key = 0; key < 12; key++) {
      Song song = new Song(key);
      NeuralNetOutput output = net.run(song);
      output.setCorrectAnswer(song.getKeyOfSong());
      displayResults(output);
    }
  }

  /**
   * Test neural network with smaller subsets of the output.
   * @param net - neural network
   * @param maxIt - specified training iterations
   * @param resetRate - rate at which the data is displayed
   */
  private static void normalTest(NeuralNet net, int maxIt, int resetRate) {
    // Train neural network for x iterations
    for (int iterations = 0; iterations < maxIt; ++iterations) {
      Song song = new Song();
      NeuralNetOutput output = net.run(song);
      output.setCorrectAnswer(song.getKeyOfSong());
      if ((iterations % resetRate) == 0) {
        displayPercentages(output);
        output.resetCounters();
      }
    }
    System.out.println("\nFinal test...");
    for (int key = 0; key < 12; key++) {
      Song song = new Song(key);
      NeuralNetOutput output = net.run(song);
      output.setCorrectAnswer(song.getKeyOfSong());
      displayResults(output);
    }
  }

  /**
   * Displays results: percent correct, how iteration number, etc.
   */
  private static void displayPercentages(final NeuralNetOutput output) {
    System.out.print("\r");
    System.out.printf("Iterations: %,d     Correct: %2.2f%%     Error: %8.5f%%",
      output.getAccumulativeIterations(),
      output.getPercentCorrect(),
      output.getError());
  }

  /**
   * Displays results: key of the song, neural network's guess, and its error.
   * @param output - output results from the neural net
   */
  private static void displayVerbose(final NeuralNetOutput output) {
    String s = (output.isCorrect()) ? "+" : " ";
    System.out.println
      ("------------------------------------------------------------------------");
    System.out.print("answer:\t" + Conversions.numbersToLetters(output
      .getCorrectAnswer()));
    System.out.print("\t|\tnet's guess:\t" + Conversions.numbersToLetters
      (output.getGuess()));
    System.out.printf("\t|\terr:\t%8.5f  %s\n", output.getError(), s);
  }

  /**
   * Displays results: key of the song, neural network's guess, and its error.
   * @param output - output results from the neural net
   */
  private static void displayResults(final NeuralNetOutput output) {
    String s = (output.isCorrect()) ? "+" : " ";
    System.out.println("answer: " +
      Conversions.numbersToLetters(output.getCorrectAnswer()) +
      "  net's guess: " +
      Conversions.numbersToLetters(output.getGuess()) +
      " " + s);
  }

  /**
   * Displays help information general purpose and commandline arguments.
   */
  private static void displayHelp() {
    System.out.println("Key Signature Identifying Neural Network");
    System.out.println("A neural network that can learn identify the key " +
      "signature of a given melody");
    System.out.println("(https://github" +
      ".com/zakrywilson/key-signature-identifying-neural-network-java)");
    System.out.println("\nusage: ./run.sh [argument]\n");
    System.out.println("Arguments:");
    System.out.println("   --help   \t\t Help");
    System.out.println("   --verbose\t\t Verbose output");
    System.out.println("   -v       \t\t Verbose output");
    System.out.println();
  }

  /**
   * Validates the commandline arguments
   * and sets verbose to true if argument given.
   * @param args - commandline arguments
   */
  private static void validateCommandlineArguments(final String[] args) {
    if (args.length > 1) {
      throw new IllegalArgumentException("Invalid number of arguments! " +
        "Max of 1 permitted, " + args.length + " provided.");
    }
    if (args.length == 1) {
      if (args[0].equals("--verbose") || args[0].equals("-v")) {
        verbose = true;
      } else if (args[0].equals("--help")) {
        help = true;
      }
    }
  }
}