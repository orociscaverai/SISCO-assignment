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

    public NBodyPanel(int w, int h) {
	this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    }

    public void updateImage(PlanetsMap pm) {
	// TODO ci deve essere un modo per lavorare su un pannello di dimensioni
	// fisse come in OpenGL
	Graphics2D g2D = image.createGraphics();
	int w = Math.min(image.getWidth(), image.getHeight());
	g2D.setColor(Color.red);
	for (int i = pm.getNumBodies() - 1; i >= 0; i--) {
	    g2D.fill(new Ellipse2D.Float(pm.getPosition(i)[0], pm
		    .getPosition(i)[1], pm.getRadius(i) * w, pm.getRadius(i)
		    * w));
	}
	repaint();
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	g2.drawImage(image, 0, 0, null);
    }
}