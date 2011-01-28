package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class MandelbrotPanel extends JPanel {

	private BufferedImage image;

	public MandelbrotPanel(int w, int h) {
		this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	}

	public void updateImage(int[] rgbData) {
		int w = image.getWidth();
		int h = image.getHeight();
		image.setRGB(0, 0, w, h, rgbData, 0, w);
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(image, 0, 0, null);
	}
}
