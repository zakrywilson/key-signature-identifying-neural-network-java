/**
 * Runs the training of the neural network
 * @author zakrywilson
 * @since 01/14/16
 */
class Manager {

  /**
   * Main: runs program.
   * @param args - args not used
   */
  public static void main(String[] args) {
    int maxIterations = 10000000;
    NeuralNet net = new NeuralNet(12, 12, 12, 0.18);

    for (int iterations = 0; iterations < maxIterations; ++iterations) {
      Song song = new Song();
      NeuralNetOutput output = net.run(song);
      displayResults(output);
    }
  }

  /**
   * Displays the results: the key of the song, the neural network's guess, and its error.
   * @param output - output results from the neural net
   */
  private static void displayResults(final NeuralNetOutput output) {
    double guess = output.getGuess();
    double answer = output.getAnswer();
    double error = output.getError();

    String s = (answer == guess) ? "+" : " ";
    System.out.println("------------------------------------------------------------------------");
    System.out.print  ("answer:\t" + Conversions.numbersToLetters(answer));
    System.out.print  ("\t|\tnet's guess:\t" + Conversions.numbersToLetters(guess));
    System.out.printf ("\t|\terr:\t%8.5f  %s\n", error , s);
  }
}