/**
 * Contains the output from the neural network for post processing.
 * @author zakrywilson
 * @since 01/15/16
 */
class NeuralNetOutput {

  private double guess;
  private double answer;
  private double error;


  /**
   * Constructor.
   * @param netGuess - the guess the neural network made
   * @param correctAnswer - the correct answer (the key of the song)
   * @param netError - the total error
   */
  NeuralNetOutput(final double netGuess, final double correctAnswer, final double netError) {
    this.guess = netGuess;
    this.answer = correctAnswer;
    this.error = netError;
  }

  /**
   * Gets the net's guess.
   * @return guess
   */
  double getGuess() {
    return this.guess;
  }

  /**
   * Gets the correct answer.
   * @return correct answer
   */
  double getAnswer() {
    return this.answer;
  }

  /**
   * Gets the error.
   * @return error
   */
  double getError() {
    return this.error;
  }
}
