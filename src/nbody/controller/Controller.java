package nbody.controller;

import nbody.model.MandelbrotSet;
import gui.Complex;
import gui.nBodyView;

/**
 * Controller part of the application.
 * 
 * @author aricci
 * 
 */
public class Controller implements InputListener {

	private nBodyView view;
	private MandelbrotSet set;

	public Controller(MandelbrotSet set, nBodyView view) {
		this.set = set;
		this.view = view;
	}

	public void started(Complex c0, double diam) {
//		view.changeState("Processing...");
//		long t0 = System.currentTimeMillis();
//		set.compute(c0, diam);
//		long t1 = System.currentTimeMillis();
//		view.changeState("Terminated - Time elapsed: " + (t1 - t0));
	}

	public void stopped() {
		// what to do??
	}

}
