package nbody;

import java.util.Random;
import java.util.Vector;

public class Planets {
    private Vector<PlanetGenerics> planets;

    private static class SingletonHolder {
	private final static Planets instance = new Planets();
    }

    public static Planets getInstance() {
	return SingletonHolder.instance;
    }

    private Planets() {
	planets = new Vector<PlanetGenerics>();
    }

    /**
     * Aggiunge numBodies elementi alla lista generati in maniera casuale
     */
    public void makeRandomBodies(int numBodies) {
	Random rand = new Random();
	float mass;
	planets.clear();
	for (int i = 0; i < numBodies; i++) {
	    mass = rand.nextFloat()
	     * 0.01f
	    ;
	    planets.add(new PlanetGenerics(mass));
	}
    }

    public PlanetGenerics getPlanet(int index) {
	return planets.get(index);
    }

    public Vector<PlanetGenerics> getPlanets() {
	return planets;
    }

    public void addPlanet(PlanetGenerics p) {
	planets.add(p);
    }

    public int getNumBodies() {
	return planets.size();
    }

    public String toString() {
	String out = "";
	int i = 0;
	for (PlanetGenerics p : planets) {
	    out += "Planet " + i + "\n";
	    out += p.toString() + "\n";
	    i++;
	}
	return out;
    }
}
