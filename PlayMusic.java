package musicTheory;

import java.io.File;
import java.io.IOException;
 
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author zakrywilson
 * @since 08/31/2015
 */
public class PlayMusic implements LineListener {
	
	boolean playCompleted = false;
	
	/**
	 * Audibly plays all the notes in the array (song).
	 * @param song - all the notes to be played
	 */
	protected static void playSong(byte[] song) {
		for (int i = 0; i < song.length; i++) {
//			playNote(song[i]);
		}
	}
	
	/**
	 * Audibly plays a single note
	 * @param noteNumber
	 */
	void play(String audioFilePath) {
		File audioFile = new File(audioFilePath);
		 
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
 
            AudioFormat format = audioStream.getFormat();
 
            DataLine.Info info = new DataLine.Info(Clip.class, format);
 
            Clip audioClip = (Clip) AudioSystem.getLine(info);
 
            audioClip.addLineListener((LineListener) this);
 
            audioClip.open(audioStream);
             
            audioClip.start();
             
            while (!playCompleted) {
                // wait for the playback completes
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
             
            audioClip.close();
             
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        };
	}
	
	/**
     * Listens to the START and STOP events of the audio line.
     */
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
         
        if (type == LineEvent.Type.START) {
            System.out.println("Playback started.");
             
        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            System.out.println("Playback completed.");
        }
 
    }

	/**
	 * Used for testing
	 * @param args
	 */
	public static void main(String[] args) {
//		Song song = new Song();
//		byte[] newSong = song.getNewSong();
//
////		playSong(newSong);
//		playNote((byte)1);
		
		String audioFilePath = "/Users/zakrywilson/CircleTap.wav";
        PlayMusic player = new PlayMusic();
        player.play(audioFilePath);
	}

}
