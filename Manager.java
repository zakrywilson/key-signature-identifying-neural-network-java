/**
 * Runs the training of the neural network.
 * @author zakrywilson
 * @since 01/14/16
 */
class Manager {


  /**
   * Main: runs program by training neural network over a specified iterations.
   * @param args - command line arguments
   */
  public static void main(String[] args) {
    try {
      run(args);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("A fatal error occurred.");
    }
  }

  /**
   * Runs the program after validating command line arguments.
   * @param args - command line arguments
   */
  private static void run(final String[] args) {
    // Create command line options
    CommandLine commandline = new CommandLine();

    // Verbose option
    Option verboseOption = new Option();
    verboseOption.addShortName("v");
    verboseOption.addLongName("verbose");
    verboseOption.addDescription("Runs program in verbose mode.");
    commandline.addOption(verboseOption);

    // Reset rate option
    Option resetRateOption = new Option();
    resetRateOption.addShortName("rr");
    resetRateOption.addExpectedArgCount(1);
    commandline.addOption(resetRateOption);

    // Max iterations option
    Option maxIterationsOption = new Option();
    maxIterationsOption.addShortName("mi");
    maxIterationsOption.addExpectedArgCount(1);
    commandline.addOption(maxIterationsOption);

    // Neural network options
    Option neuralNetOptions = new Option();
    neuralNetOptions.addShortName("nn");
    neuralNetOptions.addExpectedArgCount(4);
    commandline.addOption(neuralNetOptions);

    // Help option
    commandline.createHelp(getDisplayHelp());

    // Parse command line arguments
    commandline.parse(args);

    // Get reset rate
    int resetRate;
    if (resetRateOption.isFound()) {
      String string = resetRateOption.getArgument(0);
      try {
        resetRate = Integer.decode(string);
      } catch (NumberFormatException nfe) {
        throw new IllegalArgumentException("Reset rate must be an integer.");
      }
    } else {
      resetRate = 10000;
    }

    // Get max iterations
    int maxIterations;
    if (maxIterationsOption.isFound()) {
      String string = maxIterationsOption.getArgument(0);
      try {
        maxIterations = Integer.decode(string);
      } catch (NumberFormatException nfe) {
        throw new IllegalArgumentException("Max iterations must be an integer" +
          ".");
      }
    } else {
      maxIterations = 10000000;
    }

    // Get neural network configuration
    NeuralNet net;
    if (neuralNetOptions.isFound()) {
      int inputNodes, hiddenNodes, outputNodes;
      double learningRate;
      try {
        inputNodes = Integer.decode(neuralNetOptions.getArgument(0));
        hiddenNodes = Integer.decode(neuralNetOptions.getArgument(1));
        outputNodes = Integer.decode(neuralNetOptions.getArgument(2));
      } catch (NumberFormatException nfe) {
        throw new IllegalArgumentException("Neural network layer sizes must " +
          "be integers.");
      }
      try {
        learningRate = Double.parseDouble(neuralNetOptions.getArgument(3));
      } catch (NumberFormatException nfe) {
        throw new IllegalArgumentException("Learning rate must be a double.");
      }
      net = new NeuralNet(inputNodes, hiddenNodes, outputNodes, learningRate);
    } else {
      net = new NeuralNet(12, 12, 12, 0.18);
    }

    // Run program
    if (commandline.needHelp()) {
      System.out.println(commandline.getHelp());
    } else if (verboseOption.isFound()) {
      verboseRun(net, maxIterations);
    } else {
      System.out.println(net);
      normalRun(net, maxIterations, resetRate);
    }
  }


  /**
   * Tests neural network with verbose output.
   * @param net - neural network
   * @param maxIt - specified training iterations
   */
  private static void verboseRun(NeuralNet net, int maxIt) {
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
  private static void normalRun(NeuralNet net, int maxIt, int resetRate) {
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
   * @return help instructions
   */
  private static String getDisplayHelp() {
    String string = "Key Signature Identifying Neural Network\n";
    string += "A neural network that can learn identify the key ";
    string += "signature of a given melody\n";
    string += "(https://github.com/zakrywilson/";
    string += "key-signature-identifying-neural-network-java)\n";
    string += "\nusage: ./run.sh [arguments]\n\n";
    string += "Arguments:\n";
    string += "   -h  or  --help    \t Help\n";
    string += "   -v  or  --verbose \t Verbose output\n";
    string += "   -nn [I][H][O][R]  \t Configure neural network " +
      "characteristics\n";
    string += "   -rr [X]           \t Set the reset rate to X for normal run" +
      " (non-verbose)\n";
    string += "   -mi [N]           \t Set the max training iterations to N\n";
    string += "\n";
    return string;
  }
}