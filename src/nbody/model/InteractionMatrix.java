package nbody.model;

/**
 * Struttura dati per il salvataggio delle accelerazioni parziali. Si tratta di
 * una matrice quadrata nxn dove n è il numero dei corpi. Nella cella ij è
 * contenuta l'accelerazione parziale tra il corpo i e quello j.
 * 
 * In questa maniera ogni inserimento nella matrice può avvenire in maniera
 * concorrente, senza creare interferenze.
 * 
 * Per ottenere l'accelerazione totale su un corpo i e necessario sommare tutti
 * i valori della riga i-esima.
 * */

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

    /** Restituisce l'accelerazione totale che insiste su un corpo */
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
     * Permette l'inserimento di una forza tra due pianeti
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
