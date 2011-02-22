package gui;

import nbody.Master;

/**
 * Mandelbrot Viewer - structured sequential program.
 * 
 * @author aricci
 * 
 */
public class TestGuiMain {

    public static void main(String[] args) {

	NBodyView view = new NBodyView(600, 600);
	Master master = new Master(view);
	master.start();
    }

}
