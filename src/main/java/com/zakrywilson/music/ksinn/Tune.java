package com.zakrywilson.music.ksinn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents a single song, being comprised of a series of notes/chords and a key signature to
 * which the notes and chords generally adhere.
 *
 * @author Zach Wilson
 * @version 1.0.0
 * @since 1.0.0
 */
class Tune {

    private static final Logger LOGGER = LoggerFactory.getLogger(Tune.class);

    private static final int MAX_CHORD_LENGTH = 12;
    private static final int MIN_CHORD_LENGTH = 0;

    /**
     * The key signature of the song (always major), e.g., 2 = B major, 8 = G#/Ab major
     */
    private Byte keySignature;

    /**
     * The song length, i.e., the number of chords.
     */
    private Integer songLength;

    /**
     * The notes of the song in order. Each row represents all notes played in a <i>given point in
     * time</i>. This combination of notes played at a given point in time can be generally
     * considered a "chord" by which it will be referred in this project, for simplicity sake.
     * <p>
     * This matrix can be visualized as chords (combinations of notes) plotted in time where the
     * x-axis is time.
     */
    private byte[][] notes;

    Tune() {}

    static byte generateKeySignature() {
        Random random = new Random();
        random.setSeed(System.nanoTime());
        return (byte) random.nextInt(12);
    }

    /**
     * Generates a random song length with a lower bound (inclusive) and an upper bound
     * (exclusive).
     *
     * @param min the minimum length that can possibly be returned (inclusive)
     * @param max the max length that can possibly be returned (exclusive)
     * @return a random song length between {@code min} and {@code max}, inclusive and exclusive,
     * respectfully
     */
    static int generateSongLength(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException(String.format("Min value must be strictly less than max value: min=%d, max=%d", min, max));
        }
        Random random = new Random();
        random.setSeed(System.nanoTime());
        return random.nextInt(max) + min;
    }

    static int generateSongLength() {
        Random random = new Random();
        random.setSeed(System.nanoTime());
        return random.nextInt();
    }

    static byte[][] generateNotesOfSong(byte keySignature, int songLength) {
        byte[][] notes = new byte[songLength][];
        for (int i = 0; i < notes.length; i++) {
            notes[i] = generateChord(keySignature);
        }
        return notes;
    }

    static byte[] generateChord(byte keySignature) {
        Random random = new Random();
        random.setSeed(System.nanoTime());

        // Generate a new chord size (e.g., 6 notes)
        int chordLength = random.nextInt(MAX_CHORD_LENGTH) + MIN_CHORD_LENGTH;

        // Create an array and put random notes in that correspond with the key signature
        byte[] chord = new byte[chordLength];
        for (int i = 0; i < chord.length; i++) {
            chord[i] = (byte) random.nextInt(7); // generate a new note
        }
        chord = transposeChord(chord, keySignature);
        chord = introduceKeySignatureBreak(chord, keySignature);
        return chord;
    }

    static byte[] transposeChord(byte[] chord, byte targetKeySignature) {
        for (int i = 0; i < chord.length; i++) {
            switch (chord[i]) {
                case 0: // do
                    chord[i] = (byte) ((targetKeySignature) % 12);
                    break;
                case 1: // re
                    chord[i] = (byte) ((2 + targetKeySignature) % 12);
                    break;
                case 2: // mi
                    chord[i] = (byte) ((4 + targetKeySignature) % 12);
                    break;
                case 3: // fa
                    chord[i] = (byte) ((5 + targetKeySignature) % 12);
                    break;
                case 4: // sol
                    chord[i] = (byte) ((7 + targetKeySignature) % 12);
                    break;
                case 5: // la
                    chord[i] = (byte) ((9 + targetKeySignature) % 12);
                    break;
                case 6: // ti
                    chord[i] = (byte) ((11 + targetKeySignature) % 12);
                    break;
                default:
                    throw new RuntimeException("Invalid number in array -- does not correspond to a music note: " + chord[i]);
            }
        }

        return chord;
    }

    static byte[] introduceKeySignatureBreak(byte[] chord, byte keySignature) {
        return chord; // TODO implement
    }

    byte getKeySignature() {
        return keySignature;
    }

    @Nonnull
    Tune setKeySignature(byte keySignature) {
        if (keySignature < 0 || keySignature > 11) {
            throw new IllegalArgumentException("Key signature must be with in range: (0 through 11): " + keySignature);
        }
        this.keySignature = keySignature;
        return this;
    }

    @Nullable
    Integer getSongLength() {
        return songLength;
    }

    @Nullable
    Tune setSongLength(int songLength) {
        if (songLength < 1) {
            throw new IllegalArgumentException("Song length must be greater than 0: " + songLength);
        }
        this.songLength = songLength;
        return this;
    }

    @Nullable
    byte[][] getNotes() {
        return notes;
    }

    @Nonnull
    Tune setNotes(byte[][] notes) {
        if (notes == null) {
            throw new IllegalArgumentException("Notes matrix must not be null");
        }
        if (notes.length < 1) {
            throw new IllegalStateException("Notes matrix must contain at least one note/chord: " + Arrays.toString(notes));
        }
        this.notes = notes;
        return this;
    }

    void generateSong() {
        if (keySignature == null) { // no custom key signature was provided
            LOGGER.debug("No key signature was provided: creating one");
            keySignature = generateKeySignature();
            LOGGER.debug("Key signature was created: {}", keySignature);
        }
        if (songLength == null) { // no custom song length was provided
            LOGGER.debug("No song length was provided: creating one");
            songLength = generateSongLength();
            LOGGER.debug("Song length was created: {}", songLength);
        }
        if (notes == null) { // no notes were provided
            LOGGER.debug("No notes were provided: creating them");
            notes = generateNotesOfSong(keySignature, songLength);
            LOGGER.debug("Notes were created");
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Notes of song: {}", Arrays.stream(notes).map(Arrays::toString).collect(Collectors.joining(",", "{", "}")));
            }
        }
    }

}
