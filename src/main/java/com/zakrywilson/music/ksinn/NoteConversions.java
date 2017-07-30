package com.zakrywilson.music.ksinn;

/**
 * Handles all note conversions from numerical representations to alphabetical representations.
 *
 * @author Zach Wilson
 * @since 0.0.0
 * @version 1.0.0
 */
class NoteConversions {

    /**
     * All note names in their respective order with spacing for output
     */
    private static final String[] NOTES = {
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

    /**
     * Takes a single note number and converts it to its respective letter.
     *
     * @param value the numerical representation of the note letter
     * @return note letter
     * @throws IllegalArgumentException if the value is out of range (0 through 11)
     */
    static String numbersToLetters(int value) {
        if (value < 0 || value > 11) {
            throw new IllegalArgumentException("Invalid note number provided: " + value);
        }
        return NOTES[value];
    }

    /**
     * Takes a single note number and converts it to its respective note/letter. Note that the
     * {@code double} is case to an integer before being mapped to its respective note/letter.
     *
     * @param value the numerical representation of the note letter
     * @return note letter
     * @throws IllegalArgumentException if the value is out of range (0 through 11)
     */
    static String numbersToLetters(double value) {
        int number = (int) value; // preserve original value in case of a thrown exception
        if (number < 0 || number > 11) {
            throw new IllegalArgumentException("Invalid note number provided: " + value);
        }
        return NOTES[number];
    }

}
