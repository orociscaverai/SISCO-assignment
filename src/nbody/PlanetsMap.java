package nbody;

public class PlanetsMap {
    private final static int dimension = 2;
    private float[][] positions;
    private int numBody;

    public PlanetsMap(int numBody) {
	this.numBody = numBody;
	positions = new float[numBody][dimension];
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
	for (int i = 0; i < numBody; i++) {
	    for (int k = 0; k < dimension; k++) {
		positions[i][k] = (float) Math.random();
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
