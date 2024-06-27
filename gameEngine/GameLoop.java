//Tom Sellers
package gameEngine;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameLoop {

	Window window;

	private InputStream is;
	private InputStreamReader isr;
	private BufferedReader br;
	
	Map map = null;

	public GameLoop() throws UnsupportedAudioFileException, LineUnavailableException {
		try {
			is = getClass().getResourceAsStream("/resources/index");
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
		} catch (NullPointerException e) {
			System.out.println("Could not find index, is it there?");
			// e.printStackTrace();
			System.exit(1);
		}

		try {

			String line;

			String title = "";

			int linePoint = 0;
			int linePoint2 = 0;

			int windowWidth = 100;
			int windowHeight = 100;

			while (br.ready()) {// will only run if there is another line
				line = br.readLine();
				if (line.equals("[title]")) {
					line = br.readLine();
					while (line.charAt(0) == '#') {
						line = br.readLine();
					}
					title = line;
				} else if (line.equals("[window size]")) {
					line = br.readLine();
					while (line.charAt(0) == '#' && line.length() > 0) {
						line = br.readLine();
					}
					if (!(Boolean.parseBoolean(line))) {
						line = br.readLine();
						while (line.charAt(0) == '#' && line.length() > 0) {
							line = br.readLine();
						}

						while (!(line.charAt(linePoint) == ' ')) {
							linePoint++;
						}
						windowWidth = Integer.parseInt(line.substring(0, linePoint));
						windowHeight = Integer.parseInt(line.substring(linePoint + 1));
					} else {
						window = new Window(title);
					}
				} else if (line.equals("[map]")) {
					line = br.readLine();
					while (line.charAt(0) == '#' && line.length() > 0) {
						line = br.readLine();
					}
					map = new Map("/resources/maps/" + line);
				}
			}

			window = new Window(title, windowWidth, windowHeight);

			window.add(map);

			map.setBounds(0, 0, 1000, 1000);

			Input input = new Input();

			input.setBounds(-1, -1, 1, 1);

			window.add(input);

			while (true) {
				input.requestFocus();
				for (int i = map.comps.length-1; i > -1; i--) {
					map.comps[i].input = input;
					map.comps[i].step();
					map.repaint();
					//map.comps[i].setBounds(map.comps[i].x, map.comps[i].y, map.comps[i].width, map.comps[i].height);
				}
				try {
					Thread.sleep((long) 16.666666);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			System.out.println("There was a problem with index, please check.");
		}
	}

}
