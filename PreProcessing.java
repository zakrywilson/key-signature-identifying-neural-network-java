/**
 * Handles getting new songs for the neural net to process.
 * @author zakrywilson
 * @since 08/31/2015
 */
class PreProcessing {

	/**
	 * Takes a <code>byte[]</code> and returns the 
	 * frequency of each note in a <code>double</code> array.
	 * @param notes - all the notes in the song
	 * @return <code>double</code> array containing frequencies of each note
	 */
	static double[] getFrequencies(byte[] notes) {
		double[] frequencies = new double[12];
    for (byte note : notes)
      frequencies[note]++;
    return frequencies;
	}
	
	/**
	 * Takes a <code>double[]</code> and returns the 
	 * frequency of each note in a <code>double</code> array.
	 * @param notes - all the notes in the song
	 * @return <code>double</code> array containing frequencies of each note
	 */
	protected static double[] getFrequencies(double[] notes) {
		double[] frequencies = new double[12];
		for (double note : notes)
      frequencies[(int) note]++;
		return frequencies;
	}
	
	/**
	 * Prints out the frequencies.
	 * @param song - a song
	 */
	private static void printFrequenciesAndKey(Song song) {
		
		double[] frequencies = getFrequencies(song.getSong());
		
		System.out.println("C:\t"  + frequencies[0]);
		System.out.println("C#:\t" + frequencies[1]);
		System.out.println("D:\t"  + frequencies[2]);
		System.out.println("D:#\t" + frequencies[3]);
		System.out.println("E:\t"  + frequencies[4]);
		System.out.println("F:\t"  + frequencies[5]);
		System.out.println("F#:\t" + frequencies[6]);
		System.out.println("G:\t"  + frequencies[7]);
		System.out.println("G#:\t" + frequencies[8]);
		System.out.println("A:\t"  + frequencies[9]);
		System.out.println("A#:\t" + frequencies[10]);
		System.out.println("B:\t"  + frequencies[11]);
		
		System.out.println("\nKey:\t" + song.getKeyOfSong());
	}
	
	/**
	 * Used for testing.
	 * @param args - args not used
	 */
	public static void main(String[] args) {
		Song song = new Song();
		printFrequenciesAndKey(song);
	}
}