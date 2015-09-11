package musicTheory;

/**
 * @author zakrywilson
 * @since 09/03/15
 */
public class NoteConversion {

	protected static final String[] noteLetters = {"C", "C#/Db", "D", "D#/Eb",
			"E", "F", "F#/Gb", "G", "G#/Ab", "A", "A#/Bb", "B"};

	protected static String convertNumbersToLetters(double value) {
		
		int number = (int) value;
		
		if (number < 0 || number > 11) {
			throw new IllegalArgumentException("Invalid note number provided: " + number);
		}
		
		return noteLetters[number];
	}
}
