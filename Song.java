package musicTheory;

import java.util.Random;

/**
 * @author zakrywilson
 * @since 08/31/2015
 */
public class Song {

	private byte[] song;
	private byte key;
	
	/**
	 * Creates a new random series of notes (song) based on a randomly generated key.
	 */
	protected Song() {
		song = transpose(generateNotes());
	}

	/**
	 * @return a new song
	 */
	protected byte[] getSong() {
		return song;
	}
	
	/**
	 * @return key of the song
	 */
	protected double getKeyOfSong() {
		return (double) key;
	}
	
	/**
	 * @return <code>int</code> array of the frequency each note is played
	 */
	protected double[] getFrequencies() {
		return NetsPreprocessing.getFrequencies(this.song);
	}

	/**
	 * @return series of random notes
	 */
	protected byte[] generateNotes() {

		byte[] notesWithinKey = new byte[getSongLength()];

		// for every index in notes, we insert a random note number
		for (int note = 0; note < notesWithinKey.length; note++) {
			notesWithinKey[note] = generateRandomNote();
		}

		return notesWithinKey;
	}

	/**
	 * Generates a random number--number of notes in a song
	 * 
	 * @return the random number between 100 and 10,000
	 */
	private int getSongLength() {
		Random randomLength = new Random();
		return randomLength.nextInt(990) + 10;
	}

	/**
	 * @return a random note between 0 and 6
	 */
	private byte generateRandomNote() {
		Random randomNote = new Random();
		return (byte) randomNote.nextInt(7);
	}

	/**
	 * Changes notes from being 0-6 to being a set 
	 * within all possible notes (0-11)
	 * 
	 * @param notes
	 * @return transposed notes
	 */
	protected byte[] transpose(byte[] notes) {

		generateRandomKey();

		for (int i = 0; i < notes.length; i++) {

			// note 0
			if (notes[i] == 0) {
				notes[i] = (byte) ((0 + key) % 12);
			}
			// note 1
			else if (notes[i] == 1) {
				notes[i] = (byte) ((2 + key) % 12);
			}
			// note 2
			else if (notes[i] == 2) {
				notes[i] = (byte) ((4 + key) % 12);
			}
			// note 3
			else if (notes[i] == 3) {
				notes[i] = (byte) ((5 + key) % 12);
			}
			// note 4
			else if (notes[i] == 4) {
				notes[i] = (byte) ((7 + key) % 12);
			}
			// note 5
			else if (notes[i] == 5) {
				notes[i] = (byte) ((9 + key) % 12);
			}
			// note 6
			else if (notes[i] == 6) {
				notes[i] = (byte) ((11 + key) % 12);
			}
			// anything more...
			else {
				throw new RuntimeException("Invalid number in array: " + notes[i]);
			}
		}
		return notes;
	}

	/**
	 * Generates a random key, denoted by a number between 0 and 11
	 * 
	 * @return
	 * @return a random key
	 */
	private void generateRandomKey() {
		Random randomKey = new Random();
		key = (byte) randomKey.nextInt(12);
	}
}
