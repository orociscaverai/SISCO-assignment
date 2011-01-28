package nbody;

public class ComputeMutualAcceleration implements Runnable
{
	private int a, b;
	private final static int dimension = 2;
	private InteractionMatrix interactionMatrix;

	public ComputeMutualAcceleration(int indexA, int indexB, InteractionMatrix interactionMatrix) {
		this.a = indexA;
		this.b = indexB;
		this.interactionMatrix = interactionMatrix;
	}

	@Override
	public void run()
	{
		// TODO controllare soft
		bodyBodyInteraction(a, b, 0.0f);
	}

	private void bodyBodyInteraction(int indexA, int indexB, float soft2)
	{
		Planet a = Planets.getPlanet(indexA);
		Planet b = Planets.getPlanet(indexB);
		float[] bi = a.getPosition();
		float[] bj = b.getPosition();
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
