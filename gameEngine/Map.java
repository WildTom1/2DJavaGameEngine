//Tom Sellers
package gameEngine;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JPanel;

public class Map extends JPanel {

	private static final long serialVersionUID = 1L;

	private InputStream is;
	private InputStreamReader isr;
	private BufferedReader br;

	int width = 100;
	int height = 100;

	GameObject[] comps = null;

	public Map(String fileName) {

		setLayout(null);

		try {
			is = getClass().getResourceAsStream(fileName);
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
		} catch (NullPointerException e) {
			System.out.println("Could not find file: " + fileName + ", is it there?");
			// e.printStackTrace();
			System.exit(1);
		}

		String line = "";

		int linePoint = 0;
		int linePoint2 = 0;

		int temp1;
		int temp2;
		int temp3;

		try {
			GameObject tempObject;
			while (br.ready()) {
				line = br.readLine();
				if (line.equals("[room size]")) {
					line = br.readLine();
					while (line.charAt(0) == '#') {
						line = br.readLine();
					}
					while (!(line.charAt(linePoint) == ' ')) {
						linePoint++;
					}
					width = Integer.parseInt(line.substring(0, linePoint));
					height = Integer.parseInt(line.substring(linePoint + 1));
				} else if (line.equals("[object]")) {
					tempObject = new GameObject();
					while (((line.length() > 5)) && (br.ready())) {
						line = br.readLine();
						if (line.equals("objend")) {
							break;
						}
						if (line.subSequence(0, 6).equals("size: ")) {// sets the object size
							linePoint = 6;
							while ((!(line.charAt(linePoint) == ','))) {// finds the spot between the two numbers
								linePoint++;
							}
							tempObject.width = Integer.parseInt(line.substring(6, linePoint));
							tempObject.height = Integer.parseInt(line.substring(linePoint + 1));
						} else if (line.subSequence(0, 7).equals("color: ")) {// loads the color for the object
							linePoint = 7;
							while ((!(line.charAt(linePoint) == ','))) {
								linePoint++;
							}
							temp1 = Integer.parseInt(line.substring(7, linePoint));
							linePoint++;
							linePoint2 = linePoint;
							while ((!(line.charAt(linePoint) == ','))) {
								linePoint++;
							}
							temp2 = Integer.parseInt(line.substring(linePoint2, linePoint));
							linePoint++;
							temp3 = Integer.parseInt(line.substring(linePoint));
							tempObject.color = new Color(temp1, temp2, temp3);
							tempObject.isColor = true;
						} else if (line.subSequence(0, 7).equals("image: ")) {// loads the image
							tempObject.setUpImage(line.substring(7));
						} else if (line.subSequence(0, 8).equals("script: ")) {// tells the object the script to use, it
																				// any
							tempObject.setUpScript(line.substring(8));
						} else if (line.subSequence(0, 10).equals("position: ")) {// sets the object's position
							linePoint = 10;
							while ((!(line.charAt(linePoint) == ','))) {
								linePoint++;
							}
							tempObject.x = Integer.parseInt(line.substring(10, linePoint));
							tempObject.y = Integer.parseInt(line.substring(linePoint + 1));
						} else if (line.subSequence(0, 11).equals("collision: ")) {// dictates the object's collision
							tempObject.canCollide = Boolean.parseBoolean(line.substring(11));
						}
					}
					tempObject.endSetUp();
					addComp(tempObject);
				}
			}
		} catch (IOException e) {
			System.out.println("There was a problem with the file: " + fileName + ", please check.");
		}

	}

	public void addComp(GameObject comp) {
		if (comps == null) {
			comps = new GameObject[1];
			comps[0] = comp;
		} else {
			GameObject[] comp2 = new GameObject[comps.length + 1];
			for (int i = 0; i < comps.length; i++) {
				comp2[i] = comps[i];
			}
			comp2[comp2.length - 1] = comp;
			comps = comp2;
		}
	}

	public void paintComponent(Graphics g) {//this gets rid of all flickering and Z-fighting; this renders all of the objects
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		int xMult;
		int yMult;
		for(int i=comps.length-1;i>-1;i--) {
			if(comps[i].isColor) {
				g2D.setColor(comps[i].color);
				g2D.fillRect(comps[i].x, comps[i].y, comps[i].width, comps[i].height);
			} else if(comps[i].image==null) {
				g2D.setColor(Color.magenta);
				g2D.fillRect(comps[i].x, comps[i].y, comps[i].width / 2, comps[i].height / 2);
				g2D.fillRect(comps[i].x + (comps[i].width/2), comps[i].y + (comps[i].height/2), comps[i].width/2, comps[i].height / 2);
				g2D.setColor(Color.black);
				g2D.fillRect(comps[i].x + (comps[i].width/2), comps[i].y, comps[i].width / 2, comps[i].height / 2);
				g2D.fillRect(comps[i].x, comps[i].y + (comps[i].height/2), comps[i].width / 2, comps[i].height / 2);
			} else {
				g2D.drawImage(comps[i].image.getImage(),comps[i].x,comps[i].y,comps[i].width,comps[i].height,null);
			}
		}
	}

}
