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

    private double zoomW;
    private double zoomH;
    private double translateW;
    private double translateH;
    private float radius;
    private final float pointSize = 0.005f;

    private BufferedImage image;
    private int w, h;

    public NBodyPanel(int w, int h) {
	this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	this.w = w;
	this.h = h;
	this.zoomW = w;
	this.zoomH = h;
	this.translateH = 0;
	this.translateW = 0;
	this.radius = pointSize;
    }

    public void updateImage(PlanetsMap pm) {

	Graphics2D g2d = image.createGraphics();
	g2d.clearRect(0, 0, w, h);
	g2d.translate(translateW, translateH);
	g2d.scale(zoomW, zoomH); // Pixel rispetto alla mia unità di misura
	for (int i = pm.getNumBodies() - 1; i >= 0; i--) {

	    if (i % 3 == 0)
		g2d.setColor(Color.red);
	    else if (i % 3 == 1)
		g2d.setColor(Color.blue);
	    else if (i % 3 == 2)
		g2d.setColor(Color.green);
	    // nella mia unità di misura
	    g2d.fill(new Ellipse2D.Float(pm.getPosition(i)[0], pm
		    .getPosition(i)[1], radius, radius));
	}
	repaint();
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2d = (Graphics2D) g;
	g2d.drawImage(image, 0, 0, null);
    }

    public void setZoom(int zoom) {
	this.zoomW = w / zoom;
	this.zoomH = h / zoom;
	this.translateW = (w - zoomW) / 2;
	this.translateH = (h - zoomH) / 2;
	this.radius = zoom * pointSize;
    }
}