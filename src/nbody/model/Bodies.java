package nbody.model;

import java.util.Random;
import java.util.Vector;


/**
 * Rappresenta la lista di tutti i corpi (classe Body) della simulazione che sto
 * effettuando. La classe è realizzata mediante il pattern Singleton dato che
 * tale elenco deve rimanere unico per all'interno dell'applicazione. Fornisce
 * un metodo per la generazione random dei dati del sincolo corpo: massa e
 * velocità.
 */
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

    public Body getBody(int index) {
	return bodies.get(index);
    }

    public Vector<Body> getBodies() {
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

	public void clear() {
		bodies.clear();
		
	}
}
