package nbody;

import java.util.Random;

public class PlanetsMap {
    private final static int dimension = 2;
    private float[][] positions;
    private int numBody;

    public PlanetsMap(int numBody) {
	this.numBody = numBody;
	positions = new float[numBody][dimension];
    }

    public float[] getPosition(int PlanetIndex) {
	return positions[PlanetIndex];
    }

    public void setPosition(int PlanetIndex, float[] pos) {
	assert (pos.length == numBody);
	positions[PlanetIndex] = pos;
    }

    public void GenerateRandomMap() {
	Random rand = new Random();
	for (int i = 0; i < numBody; i++) {
	    for (int k = 0; k < dimension; k++) {
		positions[i][k] = rand.nextFloat();
	    }
	}
    }

    public String toString() {
	String out = "";
	for (int i = 0; i < numBody; i++)
	    out += "Position: " + positions[i][0] + " " + positions[i][1]
		    + "\n";

	return out;
    }
}
