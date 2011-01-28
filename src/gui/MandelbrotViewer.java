package gui;

import nbody.controller.Controller;
import nbody.model.MandelbrotSet;

/**
 * Mandelbrot Viewer - structured sequential program.
 * 
 * @author aricci
 *
 */
public class MandelbrotViewer {
	public static void main(String[] args) {
		
		int w = 800;
		int h = 600;
		int nIter = 1000;
		
		MandelbrotSet set = new MandelbrotSet(w, h, nIter);
		nBodyView view = new nBodyView(w, h);
		set.addListener(view);
		Controller controller = new Controller(set, view);
		view.addListener(controller);
		view.setVisible(true);
	}

}
