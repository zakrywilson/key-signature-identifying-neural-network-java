/**
 * Handles determining correctness of the neural net's guesses as well as displaying results.
 * @author zakrywilson
 * @since 08/31/2015
 */
class Processor {

	/**
	 * Takes the key of the song and assigns that index to 1
	 * while keeping the rest of the indexes at 0.
	 * @param correctAnswer - the key of the song (expected answer)
	 * @return an array that contains all the correct answers for the net's output
	 */
	static double[] getExpectedResults(final double correctAnswer) {
		double[] expectedAnswers = new double[12];
		expectedAnswers[(int) correctAnswer] = 1.0;
		return expectedAnswers;
	}

	/**
	 * Displays the results as letters, not numbers
	 * @param output - output results from the neural net
   */
	static void displayResults(final NeuralNetOutput output) {
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