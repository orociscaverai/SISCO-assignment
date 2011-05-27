package nbody_distribuito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nbody_distribuito.Body;

public class BodiesMap {

    private final static int dimension = 2;

    private List<Body> bodies;

    public BodiesMap() {
	bodies = new ArrayList<Body>();
    }

    public int getDimensions() {
	return dimension;
    }

    public int getNumBodies() {
	return bodies.size();
    }

    public Body getBody(int bodyIndex) {
	return bodies.get(bodyIndex);
    }

    public void generateRandomMap(int numBodies) {

	bodies.clear();
	Random rand = new Random();

	for (int i = 0; i < numBodies; i++) {

	    float mass = rand.nextFloat() * 0.01f;

	    float[] positions = new float[dimension];

	    for (int d = 0; d < dimension; d++) {
		positions[d] = rand.nextFloat() * 10;
	    }

	    bodies.add(new Body(i, mass, positions));

	}

    }

    public String toString() {

	String out = "";
	for (int i = 0; i < bodies.size(); i++) {

	    out += "Body " + i + ": ";

	    out += " " + bodies.get(i).toString();

	    out += "\n";
	}

	return out;
    }
}
