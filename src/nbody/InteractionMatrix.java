package nbody;

// Si tratta della Struttura dati dei risultati delle accelerazioni

// TODO definirla statica visto che ne deve esistere solo una? ma se la passo di volta in volta?
public class InteractionMatrix {
    final private static int dimension = 2;
    private float[][][] matrix;
    private int numBodies;

    public InteractionMatrix(int numBodies) {
	matrix = new float[numBodies][numBodies][dimension];
	this.numBodies = numBodies;
    }

    // TODO cercare un nome più significativo per questo metodo
    public float[][] getRow(int planet) {
	return matrix[planet];
    }

    public float[] getResultAcceleration(int planet) {
	float[] out = new float[dimension];
	for (int i = 0; i < numBodies; i++) {
	    if (i != planet) {
		for (int k = 0; k < dimension; k++) {
		    out[k] += matrix[planet][i][k];
		}
	    }
	}
	return out;
    }

    /**
     * 
     * Permette l'inserimento di una forza tra due pianeti
     * 
     * @param TODO
     * 
     *            tenere presente che il pianeta B dovrà avere la forza inversa
     */
    public void setAcceleration(int planetA, int planetB, float[] acceleration) {
	this.matrix[planetA][planetB] = acceleration;
	/*
	 * for (int i = 0; i < dimension; i++) this.matrix[planetB][planetA][i]
	 * = -acceleration[i];++
	 */
    }

    public String toString() {
	String out = new String();
	for (int i = 0; i < numBodies; i++) {
	    for (int n = 0; n < numBodies; n++) {
		out += "[";
		for (int k = 0; k < dimension; k++)
		    out += matrix[i][n][k] + " ";
		out += "]";
	    }
	    out += "\n";
	}
	return out;
    }
}
