package nbody;

public class ComputeMutualAcceleration implements Runnable {
    private int a, b;
    private final static int dimension = 2;
    private InteractionMatrix interactionMatrix;
    private PlanetsMap map;
    private float softFactor;

    public ComputeMutualAcceleration(int indexA, int indexB,
	    InteractionMatrix interactionMatrix, PlanetsMap map, float softFactor) {
	this.a = indexA;
	this.b = indexB;
	this.interactionMatrix = interactionMatrix;
	this.map = map;
	this.softFactor = softFactor;
    }

    @Override
    public void run() {
	bodyBodyInteraction(a, b, softFactor);
    }

    private void bodyBodyInteraction(int indexA, int indexB, float soft2) {
	
	PlanetGenerics a = Planets.getInstance().getPlanet(indexA);
	PlanetGenerics b = Planets.getInstance().getPlanet(indexB);
	float[] bi = map.getPosition(indexA);
	float[] bj = map.getPosition(indexB);
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
	float invDistCube = (float) (1.0 / Math.sqrt(distSqr * distSqr * distSqr));

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
