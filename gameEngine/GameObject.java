//Tom Sellers
package gameEngine;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

public class GameObject {

	boolean isColor = false;
	Color color = Color.black;
	int width = 100;
	int height = 100;
	int x = 0;
	int y = 0;
	boolean canCollide = false;

	ImageIcon image = null;
	
	int objNumber = 0;

	private Var[] vars = new Var[100];

	public void endSetUp() {
		vars[0] = new Int("x", x);
		vars[1] = new Int("y", y);
		vars[2] = new Int("width", width);
		vars[3] = new Int("height", height);
		vars[4] = new Int("objNumber", objNumber);
	}

	public void setUpImage(String imgFile) {
		// System.out.println("/resources/images/" + imgFile);
		if (image == null) {
			image = new ImageIcon(this.getClass().getResource("/resources/images/" + imgFile));// I always forget the
			// getResource
			isColor = false;
		} else if (!(image.toString().equals("/resources/images/" + imgFile))) {
			image = new ImageIcon(this.getClass().getResource("/resources/images/" + imgFile));
			isColor = false;
		}
	}

	// String scriptFile = null;

	private InputStream is;
	private InputStreamReader isr;
	private BufferedReader br;

	// private String scriptLine;

	private String[] scriptCode = { "" };// this is so that the program will not crash if no script is present

	public void setUpScript(String scriptFile) {
		is = getClass().getResourceAsStream("/resources/scripts/" + scriptFile);
		isr = new InputStreamReader(is);
		br = new BufferedReader(isr);

		int length = 0;

		try {
			while (br.ready()) {
				length++;
				br.readLine();
			}
			scriptCode = new String[length];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		is = getClass().getResourceAsStream("/resources/scripts/" + scriptFile);
		isr = new InputStreamReader(is);
		br = new BufferedReader(isr);

		try {
			for (int i = 0; i < scriptCode.length; i++) {
				scriptCode[i] = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	Input input = new Input();

	private String tempString;

	private Clip[] sounds = new Clip[255];//allows for the program to run up to 100 tracks	public void paintComponent(Graphics g) {


	public void step() throws UnsupportedAudioFileException, IOException, LineUnavailableException {// this intreprets the script for the gameobject
		intrepret(scriptCode);
		cleanUpSounds();
	}

	public void stopAllSounds() {
		for(int i=0;i<255;i++) {
			if(!(sounds[i]==null)) {
				sounds[i].stop();
				sounds[i] = null;
			}
		}
	}

	private void cleanUpSounds() {
		for(int i=0;i<255;i++) {
			if(!(sounds[i]==null)) {
				if(!sounds[i].isActive()) {
					sounds[i]=null;
				}
			}
		}
	}

	public void intrepret(String[] scriptCode) throws UnsupportedAudioFileException, IOException, LineUnavailableException {//this will call itself in some instances to run other parts of the script, or other scripts
		Int tmpInt;
		int tmpInteger = 0;
		int tmpInteger2 = 0;
		boolean wasVar = false;
		for (int i = 0; i < scriptCode.length; i++) {
			wasVar = false;
			for (int o = 0; o < vars.length; o++) {
				if (vars[o] == null) {
					break;
				} else {
					if (scriptCode[i].contains(vars[o].name)) {
						if (vars[o].name.equals(scriptCode[i].substring(0, vars[o].name.length()))) {
							wasVar = true;
							if (vars[o].type.equals("int")) {
								tmpInt = (Int) vars[o];
								if (scriptCode[i].substring(vars[o].name.length(), vars[o].name.length() + 2).contains("=")) {
									if (scriptCode[i].contains(" ")) {
										tmpInt.value = Integer.parseInt(scriptCode[i].substring(vars[o].name.length() + 3));
									} else {
										tmpInt.value = Integer.parseInt(scriptCode[i].substring(vars[o].name.length() + 1));
									}
								} else if (scriptCode[i].substring(vars[o].name.length(), vars[o].name.length() + 2)
										.contains("+")) {
									if (scriptCode[i].contains("++")) {
										tmpInt.value++;
									} else if (scriptCode[i].contains(" ")) {
										tmpInt.value += Integer.parseInt(scriptCode[i].substring(vars[o].name.length() + 3));
									} else {
										tmpInt.value += Integer.parseInt(scriptCode[i].substring(vars[o].name.length() + 1));
									}
								} else if (scriptCode[i].substring(vars[o].name.length(), vars[o].name.length() + 2)
										.contains("-")) {
									if (scriptCode[i].contains("--")) {
										tmpInt.value--;
									} else if (scriptCode[i].contains(" ")) {
										tmpInt.value -= Integer.parseInt(scriptCode[i].substring(vars[o].name.length() + 3));
									} else {
										tmpInt.value -= Integer.parseInt(scriptCode[i].substring(vars[o].name.length() + 1));
									}
								}
								vars[o] = tmpInt;
							}
						}
					}
				}
			}
			if (scriptCode[i].length() < 3 || wasVar) {
			} else if (scriptCode[i].substring(0, 3).contains("if ")) {// handles if statements
				tmpInteger2 = 0;
				for (int o = i + 1; o < scriptCode.length; o++) {
					if (scriptCode[o].contains("if ")) {
						tmpInteger2++;
					} else if (scriptCode[o].equals("fi")) {
						if (tmpInteger2 == 0) {
							tmpInteger = o;
							break;
						} else {
							tmpInteger--;
						}
					}
				}
				if (scriptCode[i].substring(3, 9).equals("input ")) {
					if (input.keys[Integer.parseInt(scriptCode[i].substring(9))]) {
						intrepret(scriptSplicer(scriptCode, i + 1, tmpInteger));
					}
				}
				i = tmpInteger;
			} else if (scriptCode[i].substring(0, 4).equals("log ")) {
				System.out.println(scriptCode[i].substring(4));
			} else if (scriptCode[i].substring(0, 4).equals("exit")) {
				break;
			} else if (scriptCode[i].substring(0, 5).equals("jump ")) {// a side effect of the way I did this allows for there to be function names with spaces
				tempString = scriptCode[i].substring(5);
				for (int o = 0; o < scriptCode.length; o++) {
					if (scriptCode[o].equals("funct " + tempString)) {
						i = 0;
						break;
					} else if (o == scriptCode.length - 1) {
						System.out.println("could not find function \"" + tempString + "\" called from line: " + (3 + 1));
						scriptCode = new String[1];
						scriptCode[0] = "";
					}
				}
			} else if (scriptCode[i].substring(0, 7).equals("jumpTo ")) {
				tmpInteger = Integer.parseInt(scriptCode[i].substring(7));
				i = tmpInteger;
			} else if (scriptCode[i].substring(0, 9).equals("setImage ")) {
				setUpImage(scriptCode[i].substring(9));
			} else if (scriptCode[i].substring(0, 9).equals("setColor ")) {
				color = new Color(Integer.parseInt(scriptCode[i].substring(9, scriptCode[i].indexOf(','))),Integer.parseInt(scriptCode[i].substring(scriptCode[i].indexOf(',') + 1,scriptCode[i].lastIndexOf(','))),Integer.parseInt(scriptCode[i].substring(scriptCode[i].lastIndexOf(',') + 1)));
				isColor = true;
			} else if (scriptCode[i].substring(0, 10).equals("playSound ")) {
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(GameObject.class.getResource("/resources/sound/" + scriptCode[i].substring(10)));
				Clip clip = AudioSystem.getClip();
				clip.open(audioIn);
				for(int o=0;o<255;o++) {
					if(sounds[o]==null) {
						sounds[o] = clip;
						clip.start();
						break;
					}
				}
			} else if(scriptCode[i].equals("programStop")) {
				System.exit(0);
			} else if(scriptCode[i].equals("stopAllSounds")) {
				stopAllSounds();
			}
		}
		tmpInt = (Int) vars[0];
		x = tmpInt.value;
		tmpInt = (Int) vars[1];
		y = tmpInt.value;
		tmpInt = (Int) vars[2];
		width = tmpInt.value;
		tmpInt = (Int) vars[3];
		height = tmpInt.value;
		//repaint();
	}

	public String[] scriptSplicer(String[] script, int start, int end) {
		String[] scriptString = new String[end - start];
		for (int i = 0; i < scriptString.length; i++) {
			scriptString[i] = script[i + start];
		}
		return scriptString;
	}

}
