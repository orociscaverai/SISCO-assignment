package nbody_distribuito.worker;

import java.util.concurrent.Callable;

import nbody_distribuito.master.ClientData;
import nbody_distribuito.master.ClientResponse;

public class ComputeNewPosition implements Callable<ClientResponse> {
    final private static int dimension = 2;
    private int bodyIndex;
    private float deltatime;
    private InteractionMatrix interactionMatrix;
	private int absoluteIndex;

    public ComputeNewPosition(ClientData c, float deltaTime,
	    InteractionMatrix interactionMatrix) {
	this.interactionMatrix = interactionMatrix;
	this.bodyIndex = c.getRelativeId();
	this.deltatime = deltaTime;
	this.absoluteIndex = c.getId();
    }

    private float[] computePartialVelocity(float[] acceleration) {

	float[] partialVel = new float[dimension];

	for (int i = 0; i < dimension; i++) {
	    partialVel[i] = (acceleration[i] * deltatime);
	}
	return partialVel;
    }

    private float[] computePartialDisplacement(float[] acceleration) {

	float[] partialDisplacement = new float[dimension];

	// System.out.println("Init:" + newPos[0] + " " + newPos[1]);
	for (int d = 0; d < dimension; d++) {
	    partialDisplacement[d] = 0.5f * acceleration[d] * deltatime * deltatime;
	}

	return partialDisplacement;
    }

	@Override
	public ClientResponse call() throws Exception {
		float[] acc = interactionMatrix.getResultAcceleration(bodyIndex);
		float[] vel = computePartialVelocity(acc);
		float[] displacement = computePartialDisplacement(acc);
		return new ClientResponse(absoluteIndex, vel, displacement);
	}

}
