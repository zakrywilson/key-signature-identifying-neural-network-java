/**
 * Contains the output from the neural network for post processing.
 * @author zakrywilson
 * @since 01/15/16
 */
class NeuralNetOutput {

  private double guess;
  private double error;


  /**
   * Constructor.
   * @param neuralNetworkGuess - the guess the neural network made
   * @param neuralNetworkError - the total error
   */
  NeuralNetOutput(final double neuralNetworkGuess, final double neuralNetworkError) {
    this.guess = neuralNetworkGuess;
    this.error = neuralNetworkError;
  }

  /**
   * Gets the net's guess.
   * @return guess
   */
  double getGuess() {
    return this.guess;
  }

  /**
   * Gets the error.
   * @return error
   */
  double getError() {
    return this.error;
  }
}
