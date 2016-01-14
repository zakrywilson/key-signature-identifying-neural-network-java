/**
 * Runs the training of the neural network
 * @author zakrywilson
 * @since 01/14/16
 */
class Manager {

  /**
   * Main.
   * Gets instance of
   * @param args - args not used
   */
  public static void main(String[] args) {
    NeuralNet net = NeuralNet.getInstance();
    net.run();
  }
}
