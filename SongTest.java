package musicTheory;

//import static org.junit.Assert.*;


import org.junit.Test;

public class SongTest {

	@Test
	protected void testGenerateNotes() {
		
		for (int iterations = 0; iterations < 10000; iterations++) {
			
			Song song = new Song();
			byte[] notes = song.generateNotes();

			for (int i = 0; i < notes.length; i++) {
				if (notes[i] < 0 || notes[i] > 6) 
					throw new IllegalArgumentException();
			}
		}
		System.out.println("testGenerateNotes passed.");
	}
	
	@Test
	protected void testTranspose() {
		
		for (int iterations = 0; iterations < 10000; iterations++) {
			Song song = new Song();
			byte[] notes = song.generateNotes();
			byte[] newSong = song.transpose(notes);

			for (int i = 0; i < newSong.length; i++) {
				if (newSong[i] < 0 || newSong[i] > 11) 
					throw new IllegalArgumentException();
			}
		}
		System.out.println("testTranspose passed.");
	}
	
	@Test
	protected void testSongConstuctor() {
		
		for (int iterations = 0; iterations < 10000; iterations++) {
			Song song = new Song();
			byte[] notes = song.getSong();

			for (int i = 0; i < notes.length; i++) {
				if (notes[i] < 0 || notes[i] > 11) 
					throw new IllegalArgumentException();
			}
		}
		System.out.println("testSongConstructor passed.");
	}
	
	public static void main(String[] args) {
		SongTest st = new SongTest();
		
		// testGenerateNotes
		st.testGenerateNotes();
		
		// testTranspose
		st.testTranspose();
		
		// testSongConstructor
		st.testSongConstuctor();
	}
	

}
