/**
 * Contains the output from the neural network for post processing.
 * @author zakrywilson
 * @since 01/15/16
 */
class NeuralNetOutput {


  private double guess, error, correctAnswer;
  private boolean correct;
  private static int accumulativeIterations = -1;
  private static int iterations = -1;
  private static int correctCount = 0;


  /**
   * Constructor.
   * @param neuralNetworkGuess - the guess the neural network made
   * @param neuralNetworkError - the total error
   */
  NeuralNetOutput(final double neuralNetworkGuess, final double neuralNetworkError) {
    iterations++;
    accumulativeIterations++;
    this.guess = neuralNetworkGuess;
    this.error = neuralNetworkError;
  }

  /**
   * Gets the net's guess.
   * @return neural network's guess
   */
  double getGuess() {
    return this.guess;
  }

  /**
   * Gets the standard error.
   * @return standard error
   */
  double getError() {
    return (float) this.error * (float) 100.0;
  }

  /**
   * Gets correct answer.
   *
   * @return correct answer (key signature)
   */
  double getCorrectAnswer() {
    return this.correctAnswer;
  }

  /**
   * Gets iteration count.
   *
   * @return accumulative iterations
   */
  int getAccumulativeIterations() {
    return accumulativeIterations;
  }

  /**
   * Gets the percent correct.
   *
   * @return percent correct for a set of iterations
   */
  float getPercentCorrect() {
    return (float) correctCount / (float) iterations * (float) 100.0;
  }

  /**
   * Set the correct answer.
   *
   * @param correctAnswer – the correct answer (key signature)
   */
  void setCorrectAnswer(final double correctAnswer) {
    this.correctAnswer = correctAnswer;
    if (correctAnswer == guess) {
      this.correct = true;
      correctCount++;
    }
  }

  /**
   * Reset iteration count and correct count.
   */
  void resetCounters() {
    iterations = 0;
    correctCount = 0;
  }

  /**
   * Returns whether the neural net was correct.
   *
   * @return true if neural network is correct
   */
  boolean isCorrect() {
    return this.correct;
  }
}
