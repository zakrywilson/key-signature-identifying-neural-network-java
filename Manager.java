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
    NeuralNet net = new NeuralNet(12, 12, 12);

    for (int iterations = 0; iterations < maxIterations; ++iterations) {
      Song song = new Song();
      NeuralNetOutput output = net.run(song);
      Processor.displayResults(output);
    }
  }
}