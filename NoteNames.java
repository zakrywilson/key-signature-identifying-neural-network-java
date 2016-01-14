/**
 * @author zakrywilson
 * @since 08/31/2015
 */
public class NoteNames {
	
	String[] A  = new String[]{"A",  "B",  "C#", "D",  "E",  "F#", "G#"};
	String[] Bb = new String[]{"Bb", "C",  "D",  "Eb", "F",  "G",  "A" };
	String[] B  = new String[]{"B",  "C#", "D#", "E",  "F#", "G#", "A#"};
	String[] C  = new String[]{"C",  "D",  "E",  "F",  "G",  "A",  "B" };
	String[] D  = new String[]{"D",  "E",  "F#", "G",  "A",  "B",  "C#"};
	String[] Eb = new String[]{"Eb", "F",  "G",  "Ab", "Bb", "C",  "E" };
	String[] E  = new String[]{"E",  "F#", "G#", "A",  "B",  "C#", "D#"};
	String[] F  = new String[]{"F",  "G",  "A",  "Bb", "C",  "D",  "E" };
	String[] Gb = new String[]{"Gb", "Ab", "Bb", "Cb", "Db", "Eb", "F" };
	String[] G  = new String[]{"G",  "A",  "B",  "C",  "D",  "E",  "F#"};
	String[] Ab = new String[]{"Ab", "Bb", "C",  "Db", "Eb", "Fb", "G" };
	
	protected static String[] convertToNoteNames(double keyNumber, byte[] newSong) {
		String[] convertedSong = new String[newSong.length];
		for (int i = 0; i < newSong.length; i++) {
		}
		return convertedSong;
	}
	
//	private static String[] setOfNoteNames() {
//		
//	}

	/**
	 * Used for testing
	 * @param args
	 */
	public static void main(String[] args) {
		Song song = new Song();
		byte[] newSong = song.getSong();
		double keyNumber = song.getKeyOfSong();
		
		// convert it
		String[] songWithNoteNames = convertToNoteNames(keyNumber, newSong);

		for (int i = 0; i < songWithNoteNames.length; i++) {
			System.out.println("Note number " + newSong[i] + " = " + songWithNoteNames[i]);
		}
	}

}
