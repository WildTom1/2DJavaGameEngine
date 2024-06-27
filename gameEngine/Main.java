//Tom Sellers
package gameEngine;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Main {

	public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException {
		System.setProperty("sun.java2d.opengl", "true");
		GameLoop loop = new GameLoop();
	}

}
