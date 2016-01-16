/**
 * Handles all note conversions from numerical representations to alphabetical representations.
 * @author zakrywilson
 * @since 09/03/15
 */
class Conversions {

  private static final String[] noteLetters = {
    "C    ",
    "C#/Db",
    "D    ",
    "D#/Eb",
    "E    ",
    "F    ",
    "F#/Gb",
    "G    ",
    "G#/Ab",
    "A    ",
    "A#/Bb",
    "B    "
  };

  static String numbersToLetters(double value) {
    int number = (int) value;
    if (number < 0 || number > 11) {
      throw new IllegalArgumentException("Invalid note number provided: " + number);
    }
    return noteLetters[number];
  }
}
