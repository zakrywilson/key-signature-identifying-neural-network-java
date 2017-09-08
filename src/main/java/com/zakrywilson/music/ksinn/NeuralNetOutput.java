package com.zakrywilson.music.ksinn;

/**
 * Contains the output from the neural network for post processing.
 *
 * @author Zach Wilson
 * @since 0.0.0
 * @version 1.0.0
 */
class NeuralNetOutput {

    /**
     * The neural network's guess
     */
    private double guess;

    /**
     * The neural network's error
     */
    private double error;

    /**
     * The key signature of the song
     */
    private double correctAnswer;

    /**
     * Whether the neural network's guess was correct
     */
    private boolean correct;

    /**
     * The accumulative iterations
     */
    private static int accumulativeIterations = -1;

    /**
     * The iterations that can be reset
     */
    private static int iterations = -1;

    /**
     * The number of correct answers by the neural network
     */
    private static int correctCount = 0;

    /**
     * Constructor.
     *
     * @param neuralNetworkGuess the guess the neural network made
     * @param neuralNetworkError the total error
     */
    NeuralNetOutput(final double neuralNetworkGuess, final double neuralNetworkError) {
        iterations++;
        accumulativeIterations++;
        this.guess = neuralNetworkGuess;
        this.error = neuralNetworkError;
    }

    /**
     * Gets the net's guess.
     *
     * @return neural network's guess
     */
    double getGuess() {
        return this.guess;
    }

    /**
     * Gets the standard error.
     *
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

    static void resetAll() {
        iterations = -1;
        accumulativeIterations = -1;
        correctCount = 0;
    }
}
