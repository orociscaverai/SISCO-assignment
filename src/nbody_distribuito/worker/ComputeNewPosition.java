package nbody_distribuito.worker;

import nbody_distribuito.Bodies;
import nbody_distribuito.BodiesMap;
import nbody_distribuito.Body;

public class ComputeNewPosition implements Runnable {
    final private static int dimension = 2;
    private int bodyIndex;
    private float deltatime;
    private InteractionMatrix interactionMatrix;
    private float[] oldPos;
    private BodiesMap map;

    public ComputeNewPosition(int bodyIndex, float[] oldPos, float deltaTime,
	    InteractionMatrix interactionMatrix, BodiesMap map) {
	this.interactionMatrix = interactionMatrix;
	this.bodyIndex = bodyIndex;
	this.deltatime = deltaTime;
	this.map = map;
	this.oldPos = oldPos;
    }

    @Override
    public void run() {
	float[] acc = interactionMatrix.getResultAcceleration(bodyIndex);

	Body old = Bodies.getInstance().getPlanet(bodyIndex);
	float[] vel = old.getVelocity();

	old.setVelocity(computeVelocity(acc, vel));

	map.setPosition(bodyIndex, computePosition(acc, vel, oldPos));


    }

    private float[] computeVelocity(float[] acceleration, float[] vel) {

	float[] newVel = new float[dimension];

	for (int i = 0; i < dimension; i++) {
	    newVel[i] = ((acceleration[i] * deltatime) + vel[i]);
	}
	return newVel;
    }

    private float[] computePosition(float[] acceleration, float[] vel,
	    float[] oldPos) {

	float[] newPos = new float[dimension];

	// System.out.println("Init:" + newPos[0] + " " + newPos[1]);
	for (int d = 0; d < dimension; d++) {
	    newPos[d] = 0.5f * acceleration[d] * deltatime * deltatime + vel[d]
		    * deltatime + oldPos[d];
	}

	return newPos;
    }

}