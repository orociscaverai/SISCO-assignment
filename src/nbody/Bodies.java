package nbody;

import java.util.Random;
import java.util.Vector;


public class Bodies {
    private Vector<Body> bodies;

    private static class SingletonHolder {
	private final static Bodies instance = new Bodies();
    }

    public static Bodies getInstance() {
	return SingletonHolder.instance;
    }

    private Bodies() {
	bodies = new Vector<Body>();
    }

    /**
     * Aggiunge numBodies elementi alla lista generati in maniera casuale
     */
    public void makeRandomBodies(int numBodies) {
	Random rand = new Random();
	float mass;
	bodies.clear();
	for (int i = 0; i < numBodies; i++) {
	    mass = rand.nextFloat() * 0.01f;
	    bodies.add(new Body(mass));
	}
    }

    public Body getPlanet(int index) {
	return bodies.get(index);
    }

    public Vector<Body> getPlanets() {
	return bodies;
    }

    public void addPlanet(Body p) {
	bodies.add(p);
    }

    public int getNumBodies() {
	return bodies.size();
    }

    public String toString() {
	String out = "";
	int i = 0;
	for (Body p : bodies) {
	    out += "Body " + i + ": ";
	    out += p.toString() + "\n";
	    i++;
	}
	return out;
    }
}
