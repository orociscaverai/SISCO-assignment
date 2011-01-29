package nbody;

public class ComputeNewPosition implements Runnable {
	final private static int dimension = 2;
	private int bodyIndex, deltatime;
	private InteractionMatrix interactionMatrix;
	private PlanetsMap newMap;
	private PlanetsMap oldMap;

	public ComputeNewPosition(int bodyIndex, int deltaTime,
			InteractionMatrix interactionMatrix,PlanetsMap oldMap, PlanetsMap newMap) {
		this.interactionMatrix = interactionMatrix;
		this.bodyIndex = bodyIndex;
		this.deltatime = deltaTime;
		this.oldMap = oldMap;
		this.newMap = newMap;
	}

	@Override
	public void run() {
		float[] acc = interactionMatrix.getResultAcceleration(bodyIndex);
		Planet old = oldMap.getPlanet(bodyIndex);
		float[] vel = old.getVelocity();
		float[] oldPos = old.getPosition();
		Planet p = new Planet(old.getRadius(),old.getMass());
		p.setPosition(computePosition(acc, vel,oldPos));
		p.setVelocity(computeVelocity(acc, vel));
		//TODO problemi di corse critiche utilizzare una struttura dati diversa dalla lista
		newMap.addPlanet(p);
	}

	private float[] computeVelocity(float[] acceleration, float[] oldVel) {
		float[] newVel = new float[dimension];
		for (int i = 0; i < dimension; i++) {
			newVel[i] = acceleration[i] * deltatime + oldVel[i];
		}
		return newVel;
	}

	private float[] computePosition(float[] acceleration, float[] oldVel, float[] oldPos) {
		float[] newPos = new float[dimension];
		//System.out.println("Init:"+newPos[0]+" "+newPos[1]);
		for (int i = 0; i < dimension; i++) {
			newPos[i] = 0.5f * acceleration[i] * deltatime * deltatime
			+ oldVel[i] * deltatime + oldPos[i];
		}
		return newPos;
	}
}
