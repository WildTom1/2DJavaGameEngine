//Tom Sellers
package gameEngine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class Input extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;

	public Input() {
		addKeyListener(this);
	}

	public boolean[] keys = new boolean[65535];

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;

	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;

	}

}
