package nbody;

import java.util.Random;
import java.util.Vector;

public class Planets {
    private Vector<PlanetGenerics> planets;
    final private static int dimension = 2;
    private static Planets instance;

    public static Planets getInstance(int numBodies) {
	if (instance == null) {
	    instance = new Planets(numBodies);
	}
	return instance;
    }

    public static Planets getInstance() {
	if (instance == null) {
	    instance = new Planets();
	}
	return instance;
    }

    // TODO le liste hanno un numero massimo di elementi contenibile che è
    // uguale a Integer.MAX_VALUE

    /** Costruisce una lista di pianeti vuota */
    private Planets() {
	this(0);
    }

    /**
     * Costruisce una lista con numBodies elementi generati in maniera casuale
     */
    private Planets(int numBodies) {
	planets = new Vector<PlanetGenerics>();
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
