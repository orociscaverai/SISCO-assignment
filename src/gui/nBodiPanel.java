package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class nBodiPanel extends JPanel {

    private BufferedImage image;

    public nBodiPanel(int w, int h) {
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
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	Dimension d = getSize();
	int gridWidth = d.width / 6;
	int gridHeight = d.height / 2;

	fontMetrics = pickFont(g2, "Filled and Stroked GeneralPath", gridWidth);

	Color fg3D = Color.lightGray;

	g2.setPaint(fg3D);
	g2.draw3DRect(0, 0, d.width - 1, d.height - 1, true);
	g2.draw3DRect(3, 3, d.width - 7, d.height - 7, false);
	g2.setPaint(fg);

	int x = 5;
	int y = 7;
	int rectWidth = gridWidth - 2 * x;
	int stringY = gridHeight - 3 - fontMetrics.getDescent();
	int rectHeight = stringY - fontMetrics.getMaxAscent() - y - 2;

	// fill Ellipse2D.Double
	g2.setPaint(red);
	g2.fill(new Ellipse2D.Double(x, y, rectWidth, rectHeight));
	g2.setPaint(fg);
	g2.drawString("Filled Ellipse2D", x, stringY);

    }

    // ////////////////////////////////////////////////////////////////////

    final static int maxCharHeight = 15;
    final static int minFontSize = 6;

    final static Color bg = Color.white;
    final static Color fg = Color.black;
    final static Color red = Color.red;
    final static Color white = Color.white;

    final static BasicStroke stroke = new BasicStroke(2.0f);
    final static BasicStroke wideStroke = new BasicStroke(8.0f);

    final static float dash1[] = { 10.0f };
    final static BasicStroke dashed = new BasicStroke(1.0f,
	    BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
    Dimension totalSize;
    FontMetrics fontMetrics;

    public void init() {
	// Initialize drawing colors

    }

    FontMetrics pickFont(Graphics2D g2, String longString, int xSpace) {
	boolean fontFits = false;
	Font font = g2.getFont();
	FontMetrics fontMetrics = g2.getFontMetrics();
	int size = font.getSize();
	String name = font.getName();
	int style = font.getStyle();

	while (!fontFits) {
	    if ((fontMetrics.getHeight() <= maxCharHeight)
		    && (fontMetrics.stringWidth(longString) <= xSpace)) {
		fontFits = true;
	    } else {
		if (size <= minFontSize) {
		    fontFits = true;
		} else {
		    g2.setFont(font = new Font(name, style, --size));
		    fontMetrics = g2.getFontMetrics();
		}
	    }
	}

	return fontMetrics;
    }

    public void paint(Graphics g) {

    }

    public static void main(String s[]) {
	JFrame f = new JFrame("ShapesDemo2D");
	f.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});
	JPanel applet = new nBodiPanel(10, 0);
	f.getContentPane().add("Center", applet);
	f.pack();
	f.setSize(new Dimension(550, 100));
	f.setVisible(true);
    }

}
