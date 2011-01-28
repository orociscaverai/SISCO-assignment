package nbody.model;

import gui.Complex;
import gui.MandelbrotSetListener;

import java.util.*;

/**
 * Class representing the set to be computed - this is part of the model.
 * 
 * @author aricci
 * 
 */
public class MandelbrotSet {

    private int w, h;
    private int nIterMax;
    private int image[];
    private ArrayList<MandelbrotSetListener> listeners;

    private Complex currentCenter;
    private double currentDiameter;

    public MandelbrotSet(int w, int h, int nIterMax) {
	this.w = w;
	this.h = h;
	this.nIterMax = nIterMax;
	image = new int[w * h];
	currentCenter = null;
	currentDiameter = -1;
	listeners = new ArrayList<MandelbrotSetListener>();
    }

    public int getSizeX() {
	return w;
    }

    public int getSizeY() {
	return h;
    }

    public int[] getImage() {
	return image;
    }

    public Complex getCurrentCenter() {
	return currentCenter;
    }

    public double getCurrentDiameter() {
	return currentDiameter;
    }

    public void compute(Complex c0, double diamX) {

	// double cx, cy;
	// double diamY = diamX * h / w;
	// double dx = diamX / w;
	// double dy = diamY / h;
	// double radiusX = diamX * 0.5;
	// double radiusY = diamY * 0.5;
	// for (int x = 0; x < w; x++) {
	// for (int y = 0; y < h; y++) {
	// cx = x * dx + c0.re() - radiusX;
	// cy = c0.im() + radiusY - y * dy;
	// /*
	// * if (isPartOfTheSet(cx,cy,nIterMax)){ // black image.setRGB(x,
	// * y, 0); } else { // white image.setRGB(x, y, 0xFFFFFF); }
	// */
	// double color = computeColor(cx, cy, nIterMax);
	// int c = (int) (color * 255);
	// image[y * w + x] = c + (c << 8) + (c << 16);
	// }
	// }

	// TODO qui bisogna creare l'immagina da inviare successivamente alla
	// View
	notifyListeners();
    }

    private double computeColor(double x0, double y0, int maxIteration) {
	int iteration = 0;
	double x = x0;
	double y = y0;
	while (x * x + y * y <= 4 && iteration < maxIteration) {
	    double xtemp = x * x - y * y + x0;
	    y = 2 * x * y + y0;
	    x = xtemp;
	    iteration++;
	}
	if (iteration == maxIteration) {
	    return 0;
	} else {
	    return 1.0 - 0.001 * iteration;
	}
    }

    public void addListener(MandelbrotSetListener l) {
	listeners.add(l);
    }

    private void notifyListeners() {
	for (MandelbrotSetListener l : listeners) {
	    l.setUpdated(this);
	}
    }

    private boolean isPartOfTheSet(double x0, double y0, int maxIteration) {
	int iteration = 0;
	double x = x0;
	double y = y0;
	while (x * x + y * y <= 4 && iteration < maxIteration) {
	    double xtemp = x * x - y * y + x0;
	    y = 2 * x * y + y0;
	    x = xtemp;
	    iteration++;
	}
	if (iteration == maxIteration) {
	    return true;
	} else {
	    return false;
	}
    }
}
