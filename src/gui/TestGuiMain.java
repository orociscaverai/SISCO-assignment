package gui;

import java.util.concurrent.ArrayBlockingQueue;

import nbody.Master;
import nbody.Planets;
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
	System.setProperty("velocityDamping", "1.0");
	System.setProperty("softFactor", "1");
	System.setProperty("deltaTime", "1");
	System.setProperty("gravityConst", "1.0");

	NBodyView view = new NBodyView(800, 600, coda);
	Master master = new Master(view, coda);
	// initPlanet();
	master.start();
    }

    private static void initPlanet() {
	Planets p = Planets.getInstance();
	// p.addPlanet(new PlanetGenerics(0.638f, 0.05f));
	// p.addPlanet(new PlanetGenerics(0.174f, 0.007f));
    }

}
