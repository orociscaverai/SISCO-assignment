package nbody;

import java.util.Random;
import java.util.Vector;

public class Planets {
    private Vector<PlanetGenerics> planets;
    final private static int dimension = 2;

    private static class SingletonHolder {
	private final static Planets instance = new Planets();
    }

    // TODO singleton ThreadSafe
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
	float radius;
	for (int i = 0; i < numBodies; i++) {
	    /*
	     * float position[] = new float[dimension]; for (int k = 0; k <
	     * dimension; k++) { position[k] = rand.nextFloat(); }
	     */
	    // XXX la proporzione la considerimo 1/20
	    // TODO considerare un minimo
	    radius = rand.nextFloat() / 20;
	    // calcolare la massa della Terra aggiungere un fattore casuale per
	    // la densità
	    planets.add(new PlanetGenerics(/* position, */radius, radius * 100));
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
