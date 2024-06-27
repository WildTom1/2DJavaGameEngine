//Tom Sellers
package gameEngine;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window {

	JFrame frame = new JFrame();
	JPanel contentPane = new JPanel();

	boolean fullscreen = false;

	String title;

	Dimension size = new Dimension();

	public Window(String title, int width, int height) {
		size.width = width;
		size.height = height;
		frame.setSize(size);
		setUpWindow(title, fullscreen);
	}

	public Window(String title, Dimension size) {
		this.size = size;
		frame.setSize(size);
		setUpWindow(title, fullscreen);
	}

	public Window(String title) {
		size.width = 100;
		size.height = 100;
		setUpWindow(title, true);
	}

	private void setUpWindow(String title, boolean fullscreen) {
		this.title = title;
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setLayout(null);
		frame.add(contentPane);
		frame.setVisible(true);
	}

	public void setSize(int width, int height) {
		size.width = width;
		size.height = height;
		frame.setSize(size);
	}

	public void setSize(Dimension size) {
		frame.setSize(size);
	}

	public void setFullscreen(boolean setFullscreen) {
		if (!(fullscreen == setFullscreen)) {
			if (setFullscreen) {
				frame.setDefaultCloseOperation(0);
				frame.dispose();
				frame = new JFrame(title);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setVisible(true);
				fullscreen = true;
			} else {
				frame.setDefaultCloseOperation(0);
				frame.dispose();
				frame = new JFrame(title);
				frame.setSize(size);
				frame.setVisible(true);
				fullscreen = false;
			}
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
	}

	public void add(Component comp) {
		contentPane.add(comp);
	}

	public void remove(Component comp) {
		contentPane.remove(comp);
	}

	public void removeAll() {
		contentPane.removeAll();
	}

	public void repaint() {
		contentPane.repaint();
	}

}
