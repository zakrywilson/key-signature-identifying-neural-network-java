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
    NeuralNet net = NeuralNet.getInstance();
    net.setIterations(10000000);
    net.configure(12, 12, 12);
    net.run();
  }
}