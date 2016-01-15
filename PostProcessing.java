/**
 * Handles determining correctness of the neural net's guesses as well as displaying results.
 * @author zakrywilson
 * @since 08/31/2015
 */
class PostProcessing {
	
	/**
	 * Takes the key of the song and constructs a <code>double[]</code>
	 * that contains the correct pattern (w-w-h-w-w-w-h) so the network
	 * can accurately gauge how close it was in weighing possible answers.
	 * @param correctAnswer
	 * @return an array that contains all the correct answers for the net's output
	 */
	protected static double[] computeExpectedScale(double correctAnswer) {
		int offset = (int) correctAnswer;
		double[] expectedAnswers = new double[12];
		double[] scale = {1, 0, .25, 0, .25, .25, 0, .75, .50, .25, 0, .10};
		
		for (int i = 0; i < scale.length; i++) {
			expectedAnswers[(i + offset) % 12] = scale[i];
		}
		
		return expectedAnswers;
	}
	
	/**
	 * Takes the key of the song and assigns that index to 1
	 * while keeping the rest of the indexes at 0.
	 * @param correctAnswer
	 * @return an array that contains all the correct answers for the net's output
	 */
	static double[] computeExpectedAnswer(double correctAnswer) {
		double[] expectedAnswers = new double[12];
		expectedAnswers[(int) correctAnswer] = 1.0;
		
		return expectedAnswers;
	}
	
	/**
	 * Finds the highest scored note in <code>values[]</code> and returns that note.
	 * @param values
	 * @return net's guess
	 */
	static double interpretResults(double values[]) {
		int index = NeuralNet.sizeOfNetwork() - NeuralNet.sizeOfOutputLayer() + 1;
		double offset = index - 1;
		double guess = values[index - 1];
		double note = index - 1.0;
		double val;

		while (index < values.length) {
			val = values[index];
			if (val > guess) { 
				guess = val;
				note = index;
			}
			index++;
		}
		return note - offset;
	}
	
	/**
	 * Finds the highest scored note in <code>values[]</code> and returns that note.
	 * @param values
	 * @return net's guess
	 */
	protected static double interpretResultsAddition(double values[]) {
		int index = NeuralNet.sizeOfNetwork() - NeuralNet.sizeOfOutputLayer() + 1;
		double val = 0.0;
		
		while (index < values.length) {
			val += values[index];
			index++;
		}
		
		return val / 12;
	}
	
	protected static double findResult(double values[]) {
		double guess = values[values.length - 1]; // get last value: output node
		guess *= 12;
		return Math.round(guess) - 1;
	}
	
	/**
	 * Displays the results as letters, not numbers
	 * @param correctAnswer
	 * @param results
	 */
	static void displayNetwork(double correctAnswer, double results[]) {
		String s = " ";
		
		if (correctAnswer == results[NeuralNet.GUESS]) s = "+";
		
		System.out.println("------------------------------------------------------------------------");
		System.out.print  ("answer:\t" + NoteConversion.convertNumbersToLetters(correctAnswer));
		System.out.print  ("\t|\tnet's guess:\t" + NoteConversion.convertNumbersToLetters(results[NeuralNet.GUESS]));
		System.out.printf ("\t|\terr:\t%8.5f  %s\n", results[NeuralNet.ERROR] , s);
	}
}