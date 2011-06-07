package nbody_distribuito.worker;

import nbody_distribuito.master.ClientData;

public class ComputeMutualAcceleration implements Runnable {
    private ClientData a, b;
    private final static int dimension = 2;
    private InteractionMatrix interactionMatrix;
    private float softFactor;

    public ComputeMutualAcceleration(ClientData c1 , ClientData c2,
	    InteractionMatrix interactionMatrix, float softFactor) {
    	this.a = c1;
    	this.b = c2;
	this.softFactor = softFactor;
	this.interactionMatrix = interactionMatrix;
    }

    @Override
    public void run() {
	bodyBodyInteraction(a, b, softFactor);
    }

    private void bodyBodyInteraction(ClientData c1, ClientData c2, float soft2) {

	int indexA = c1.getRelativeId();
	int indexB = c2.getRelativeId();
	float[] bi = c1.getPos();
	float[] bj = c2.getPos();
	float[] rij = new float[dimension];

	// Distanza tra il Body i e il Body j
	for (int i = 0; i < dimension; i++)
	    rij[i] = bj[i] - bi[i];

	// Distanza al quadrato
	float distSqr = 0;
	for (int i = 0; i < dimension; i++)
	    distSqr += rij[i] * rij[i];
	distSqr += soft2;

	// invDistCube = G / distSqr^(3/2)
	float invDistCube = (float) (1.0 / Math.sqrt(distSqr * distSqr
		* distSqr));

	float[] ai = new float[dimension];
	float[] aj = new float[dimension];
	for (int i = 0; i < dimension; i++) {
	    ai[i] = rij[i] * b.getMass() * invDistCube;
	    aj[i] = rij[i] * a.getMass() * invDistCube * -1;
	}
	interactionMatrix.setAcceleration(indexA, indexB, ai);
	interactionMatrix.setAcceleration(indexB, indexA, aj);
    }

}
