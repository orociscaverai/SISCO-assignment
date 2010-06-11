package nbody;

/**
 * @author Boccacci Andrea, Cicora Saverio
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		float[] bi = { 0.1f, 0.1f, 0.1f };
		float[] bj = { 0.1f, 0.1f, 0.1f };
		float[] ai = { 0.1f, 0.1f };

		System.out.println(bodyBodyInteraction(bi, bj, ai, 0.0f));
	}

	public static float[] bodyBodyInteraction(float[] bi, float[] bj,
			float[] ai, float soft2) {
		float[] r = new float[2];

		r[0] = bj[0] - bi[0];
		r[1] = bj[1] - bi[1];

		float distSqr = r[0] * r[0] + r[1] * r[1] + soft2;
		double distSixth = distSqr * distSqr * distSqr;
		// TODO: controllare il cast double float
		float invDistCube = 1.0f / (float) Math.sqrt(distSixth);
		float s = bj[2] * invDistCube;

		ai[0] += r[0] * s;
		ai[1] += r[1] * s;
		return ai;
	}

}
