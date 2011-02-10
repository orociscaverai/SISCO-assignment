package gui;

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

	NBodyView view = new NBodyView(800, 600);
	Master master = new Master(view);
	initPlanet();
	master.start();
    }
    
    private static void initPlanet(){
	Planets p  = Planets.getInstance();
	p.addPlanet(new PlanetGenerics(0.638f, 0.05f));
	p.addPlanet(new PlanetGenerics(0.174f, 0.007f));
	p.addPlanet(new PlanetGenerics(0.638f, 0.05f));
	p.addPlanet(new PlanetGenerics(0.174f, 0.007f));
    }

}
