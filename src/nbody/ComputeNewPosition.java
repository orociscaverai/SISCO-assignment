package nbody;

public class ComputeNewPosition implements Runnable {
    final private static int dimension = 2;
    private int bodyIndex;
    private float deltatime;
    private InteractionMatrix interactionMatrix;
    private float[] oldPos;
    private PlanetsMap map;
    private float velocityDamping;

    public ComputeNewPosition(int bodyIndex, float[] oldPos, float deltaTime,
	    InteractionMatrix interactionMatrix, PlanetsMap map) {
	this.interactionMatrix = interactionMatrix;
	this.bodyIndex = bodyIndex;
	this.deltatime = deltaTime;
	this.map = map;
	this.oldPos = oldPos;
	this.velocityDamping = Float.parseFloat(System.getProperty(
		"velocityDamping", "1.0"));
    }

    @Override
    public void run() {
	float[] acc = interactionMatrix.getResultAcceleration(bodyIndex);

	PlanetGenerics old = Planets.getInstance().getPlanet(bodyIndex);
	float[] vel = old.getVelocity();

	old.setVelocity(computeVelocity(acc, vel));

	map.setPosition(bodyIndex, computePosition(acc, vel, oldPos));

    }

    private float[] computeVelocity(float[] acceleration, float[] vel) {

	float[] newVel = new float[dimension];

	for (int i = 0; i < dimension; i++) {
	    newVel[i] = ((acceleration[i] * deltatime) + vel[i])
		    * velocityDamping;
	}
	return newVel;
    }

    private float[] computePosition(float[] acceleration, float[] vel,
	    float[] oldPos) {

	float[] newPos = new float[dimension];

	// System.out.println("Init:" + newPos[0] + " " + newPos[1]);
	for (int i = 0; i < dimension; i++) {
	    newPos[i] = 0.5f * acceleration[i] * deltatime * deltatime + vel[i]
		    * deltatime + oldPos[i];
	}

	return newPos;
    }

}
