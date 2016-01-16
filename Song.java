import java.util.Random;

/**
 * Handles the generation of new songs.
 * @author zakrywilson
 * @since 08/31/2015
 */
class Song {

  private byte[] song;
  private int key;
  private Random random;


  /**
   * Creates a new random series of notes (song) based on a randomly generated key.
   */
  Song() {
    random = new Random();
    key = generateRandomKey();
    song = generateRandomSong();
  }

  /**
   * @return key of the song
   */
  double getKeyOfSong() {
    return (double) key;
  }

  /**
   * Takes a byte array and returns the frequency of each note in an array of doubles.
   * @return array of doubles containing frequencies of each note
   */
  double[] getFrequencies() {
    double[] frequencies = new double[12];
    for (byte note : this.song)
      frequencies[note]++;
    return frequencies;
  }

  /**
   * Takes the key of the song and assigns that index to 1 while keeping the rest of the indexes at 0.
   * @return an array that contains all the correct answers for the net's output
   */
  double[] getExpectedOutput() {
    double[] expectedAnswers = new double[12];
    expectedAnswers[key] = 1.0;
    return expectedAnswers;
  }

  /**
   * Creates a new random series of notes based on the key signature.
   * @return a new random song based on the key
   */
  private byte[] generateRandomSong() {
    return transpose(generateNotes());
  }

  /**
   * Changes notes from being 0-6 to being a set within all possible notes (0-11).
   * @param notes - all notes in the song
   * @return transposed notes
   */
  private byte[] transpose(byte[] notes) {

    for (int i = 0; i < notes.length; i++) {

      // note 0
      if (notes[i] == 0) {
        notes[i] = (byte) ((key) % 12);
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
   * Generates the series of random notes.
   * @return series of random notes
   */
  private byte[] generateNotes() {

    byte[] notesWithinKey = new byte[getSongLength()];

    // for every index in notes, we insert a random note number
    for (int note = 0; note < notesWithinKey.length; note++) {
      notesWithinKey[note] = generateRandomNote();
    }

    return notesWithinKey;
  }

  /**
   * Generates a random number--number of notes in a song.
   * @return the random number between 100 and 10,000
   */
  private int getSongLength() {
    return random.nextInt(990) + 10;
  }

  /**
   * Generates a random note between 0 and 6.
   * @return a random note between 0 and 6
   */
  private byte generateRandomNote() {
    return (byte) random.nextInt(7);
  }

  /**
   * Generates a random key, denoted by a number between 0 and 11.
   * @return a random key
   */
  private int generateRandomKey() {
    return random.nextInt(12);
  }
}