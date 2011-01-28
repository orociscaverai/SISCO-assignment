package nbody;

public class ComputeMutualAcceleration implements Runnable
{
	private int a, b;
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
		float[] r = new float[2];

		r[0] = bj[0] - bi[0];
		r[1] = bj[1] - bi[1];

		float distSqr = r[0] * r[0] + r[1] * r[1] + soft2;

		float invDistCube = (G / distSqr);

		float[] ai = new float[2];
		float[] aj = new float[2];
		ai[0] = (float) (invDistCube * b.getMass() * (r[0] / Math.sqrt(distSqr)));
		ai[1] = (float) (invDistCube * b.getMass() * (r[1] / Math.sqrt(distSqr)));
		aj[0] = (float) (invDistCube * a.getMass() * (-r[0] / Math.sqrt(distSqr)));
		aj[1] = (float) (invDistCube * a.getMass() * (-r[1] / Math.sqrt(distSqr)));

		interactionMatrix.setAcceleration(indexA, indexB, ai);
		interactionMatrix.setAcceleration(indexB, indexA, aj);
	}	
	
	
	
}
