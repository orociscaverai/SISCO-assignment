package gui;

import java.util.concurrent.ArrayBlockingQueue;

import nbody.Master;
import nbody.PlanetsMap;

/**
 * Mandelbrot Viewer - structured sequential program.
 * 
 * @author aricci
 * 
 */
public class TestGuiMain {

    public static void main(String[] args) {

	ArrayBlockingQueue<PlanetsMap> coda = new ArrayBlockingQueue<PlanetsMap>(
		500);
	System.setProperty("poolSize", "6");

	NBodyView view = new NBodyView(600, 600, coda);
	Master master = new Master(view, coda);
	master.start();
    }

}
