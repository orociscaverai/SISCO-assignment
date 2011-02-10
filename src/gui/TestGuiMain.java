package gui;

import java.util.Properties;

import nbody.Master;
import nbody.PlanetGenerics;
import nbody.Planets;

/**
 * Mandelbrot Viewer - structured sequential program.
 * 
 * @author aricci
 * 
 */
public class TestGuiMain {
    public static void main(String[] args) {
	System.setProperty("poolSize", "6");
	System.setProperty("velocityDamping", "0.0");
	System.setProperty("softFactor", "0.1");
	System.setProperty("deltaTime", "0.01");
	System.setProperty("gravityConst", "1.0");

	NBodyView view = new NBodyView(800, 600);
	Master master = new Master(view);
	// initPlanet();
	master.start();
    }

    private static void initPlanet() {
	Planets p = Planets.getInstance();
	p.addPlanet(new PlanetGenerics(0.0638f, 0.05f));
	p.addPlanet(new PlanetGenerics(0.0174f, 0.007f));
	// p.addPlanet(new PlanetGenerics(0.638f, 0.05f));
	// p.addPlanet(new PlanetGenerics(0.174f, 0.007f));
    }

}
