package nbody_distribuito.worker;

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

    /**
     * Restituisce la riga della matrice. Utile più che altro per motivi di
     * Debug
     */
    public float[][] getRow(int bodyIndex) {
	return matrix[bodyIndex];
    }

    public float[] getResultAcceleration(int planet) {

	float[] out = new float[dimension];
	for (int i = 0; i < numBodies; i++) {
	    if (i != planet) {
		for (int d = 0; d < dimension; d++) {
		    out[d] += matrix[planet][i][d];
		}
	    }
	}
	return out;
    }

    /**
     * 
     * Permette l'inserimento di una forza tra due pianeti
     * 
     */
    public void setAcceleration(int planetA, int planetB, float[] acceleration) {
	this.matrix[planetA][planetB] = acceleration;
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
