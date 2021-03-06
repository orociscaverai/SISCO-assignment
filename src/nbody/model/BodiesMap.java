package nbody.model;

import java.util.Random;

/**
 * La classe mantiene i dati relativi alla posizione dei corpi nello spazio. Un
 * istanza di tale classe viene inviata alla gui. Fornisce un metodo per la
 * generazione random della posizione iniziale dei corpi.
 */
public class BodiesMap {

    private final static int dimension = 2;
    private float[][] positions;
    private int numBody;

    public BodiesMap(int numBody) {
	this.numBody = numBody;
	positions = new float[numBody][dimension];
    }

    public int getDimensions() {
	return dimension;
    }

    public int getNumBodies() {
	return numBody;
    }

    public float[] getPosition(int planetIndex) {
	return positions[planetIndex];
    }

    public void setPosition(int planetIndex, float[] pos) {
	assert (pos.length == numBody);
	this.positions[planetIndex] = pos;
    }

    public void generateRandomMap() {

	Random rand = new Random();

	for (int i = 0; i < numBody; i++) {

	    for (int d = 0; d < dimension; d++) {
		positions[i][d] = rand.nextFloat() * 10;
	    }

	}

    }

    public String toString() {

	String out = "";
	for (int i = 0; i < numBody; i++) {
	    out += "Body " + i + ": Position:";

	    for (int d = 0; d < dimension; d++) {
		out += " " + positions[i][d];
	    }

	    out += "\n";
	}

	return out;
    }
}
