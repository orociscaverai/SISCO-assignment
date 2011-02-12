package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import nbody.PlanetsMap;

@SuppressWarnings("serial")
public class NBodyPanel extends JPanel {

    private BufferedImage image;
    private int w, h;

    public NBodyPanel(int w, int h) {
	this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	this.w = w;
	this.h = h;
    }

    public void updateImage(PlanetsMap pm) {
	// TODO ci deve essere un modo per lavorare su un pannello di dimensioni
	// fisse come in OpenGL
	Graphics2D g2d = image.createGraphics();
	g2d.clearRect(0, 0, w, h);
	g2d.setColor(Color.red);
	g2d.translate(w / 2, h / 2);
	g2d.scale(w / 10, h / 10); // Pixel rispetto alla mia unità di misura
	float radius = 0.05f;
	for (int i = pm.getNumBodies() - 1; i >= 0; i--) {// le cordinate sono
	    // nella mia unità di misura
	    g2d.fill(new Ellipse2D.Float(pm.getPosition(i)[0], pm
		    .getPosition(i)[1], radius, radius));
	}
	repaint();
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	g2.drawImage(image, 0, 0, null);
    }
}