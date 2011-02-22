package nbody;

public class PlanetsMap {
    private final static int dimension = 2;
    private float[][] positions;
    private int numBody;

    public PlanetsMap(int numBody) {
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

	for (int i = 0; i < numBody; i++) {

	    for (int d = 0; d < dimension; d++) {
		positions[i][d] = (float) Math.random();
	    }

	}

    }

    public String toString() {

	String out = "";
	for (int i = 0; i < numBody; i++) {
	    out += "Position body " + i + ":";

	    for (int d = 0; d < dimension; d++) {
		out += " " + positions[i][d];
	    }

	    out += "\n";
	}

	return out;
    }
}
