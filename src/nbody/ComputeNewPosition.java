package nbody;

public class ComputeNewPosition implements Runnable {
    final private static int dimension = 2;
    int bodyIndex, deltatime;
    InteractionMatrix interactionMatrix;

    public ComputeNewPosition(int bodyIndex, int deltaTime,
	    InteractionMatrix interactionMatrix) {
	this.interactionMatrix = interactionMatrix;
	this.bodyIndex = bodyIndex;
	this.deltatime = deltaTime;
    }

    @Override
    public void run() {
	float[] acc = interactionMatrix.getResultAcceleration(bodyIndex);
	float[] vel = Planets.getPlanet(bodyIndex).getVelocity();
	computePosition(acc, vel);
	computeVelocity(acc, vel);
    }

    private void computeVelocity(float[] acceleration, float[] oldVel) {
	float[] newVel = new float[dimension];
	for (int i = 0; i < dimension; i++) {
	    newVel[i] = acceleration[i] * deltatime + oldVel[i];
	}
	Planets.getPlanet(bodyIndex).setVelocity(newVel);
    }

    private void computePosition(float[] acceleration, float[] oldVel) {
	float[] newPos = new float[dimension];
	float[] oldPos = Planets.getPlanet(bodyIndex).getPosition();
	for (int i = 0; i < dimension; i++) {
	    newPos[i] = 0.5f * acceleration[i] * deltatime * deltatime
		    + oldVel[i] * deltatime + oldPos[i];
	}
	Planets.getPlanet(bodyIndex).setPosition(newPos);
    }
}
