package gui;

import nbody.ComputeMutualAcceleration;
import nbody.ComputeNewPosition;
import nbody.InteractionMatrix;
import nbody.PlanetGenerics;
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

	NBodyView view = new NBodyView(800, 600);

	Planets pl;
	pl = Planets.getInstance();

	pl.addPlanet(new PlanetGenerics(1f, 1f));
	pl.addPlanet(new PlanetGenerics(1f, 2f));

	InteractionMatrix im = new InteractionMatrix(2);
	PlanetsMap map = new PlanetsMap(2);
	PlanetsMap newMap = new PlanetsMap(2);

	float[] fl = new float[2];
	fl[0] = 0.0f;
	fl[1] = 0.0f;
	map.setPosition(0, fl, 0.009f);

	fl = new float[2];
	fl[0] = 4.0f;
	fl[1] = 3.0f;
	map.setPosition(1, fl, 0.1f);

	ComputeMutualAcceleration cma = new ComputeMutualAcceleration(0, 1, im,
		map);
	cma.run();
	System.out.println(pl.toString());
	System.out.println("-------------------------");
	System.out.println(im.toString());
	ComputeNewPosition cp1 = new ComputeNewPosition(0, map.getPosition(0),
		2, im, newMap);
	ComputeNewPosition cp2 = new ComputeNewPosition(1, map.getPosition(1),
		1, im, newMap);
	cp1.run();
	cp2.run();
	view.setUpdated(map);
	System.out.println(pl.toString());
	System.out.println("-------------------------");
	System.out.println(newMap.toString());
	System.out.println("-------------------------");

    }

}
