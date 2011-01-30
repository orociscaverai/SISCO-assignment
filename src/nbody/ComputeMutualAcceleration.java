package nbody;

public class ComputeMutualAcceleration implements Runnable
{
	private int a, b;
	private final static int dimension = 2;
	private InteractionMatrix interactionMatrix;
	private PlanetsMap map;

	public ComputeMutualAcceleration(int indexA, int indexB, InteractionMatrix interactionMatrix, PlanetsMap map) {
		this.a = indexA;
		this.b = indexB;
		this.interactionMatrix = interactionMatrix;
		this.map = map;
	}

	@Override
	public void run()
	{
		// TODO controllare soft
		bodyBodyInteraction(a, b, 0.0f);
	}

	private void bodyBodyInteraction(int indexA, int indexB, float soft2)
	{
		PlanetGenerics a = Planets.getInstance().getPlanet(indexA);
		PlanetGenerics b = Planets.getInstance().getPlanet(indexB);
		float[] bi = map.getPosition(indexA);
		float[] bj = map.getPosition(indexB);
		float G = 1.0f;
		float[] r = new float[dimension];

		for (int i = 0; i<dimension;i++)
			r[i] = bj[i] - bi[i];

		float distSqr = 0;
		for (int i=0; i<dimension; i++)
			distSqr = distSqr + r[i] * r[i];
		distSqr += soft2;

		float invDistCube = (G / distSqr);

		float[] ai = new float[dimension];
		float[] aj = new float[dimension];
		for (int i = 0; i<dimension; i++){
			ai[i] = (float) (invDistCube * b.getMass() * (r[i] / Math.sqrt(distSqr)));
			aj[i] = (float) (invDistCube * a.getMass() * (-r[i] / Math.sqrt(distSqr)));
		}
		interactionMatrix.setAcceleration(indexA, indexB, ai);
		interactionMatrix.setAcceleration(indexB, indexA, aj);
	}	



}
