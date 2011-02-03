package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

import nbody.PlanetGenerics;

@SuppressWarnings("serial")
public class NBodyPanel extends JPanel {

    private BufferedImage image;

    public NBodyPanel(int w, int h) {
	this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    }

    public void updateImage(Vector<PlanetGenerics> planets) {
	Graphics2D g2D = image.createGraphics();
	g2D.setColor(Color.red);
	for (PlanetGenerics p : planets) {
	    g2D.fill(new Ellipse2D.Float(0, 0, p.getRadius() * 800f, p
		    .getRadius() * 600f));
	}
	repaint();
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	g2.drawImage(image, 0, 0, null);
    }
}