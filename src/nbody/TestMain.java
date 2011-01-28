package nbody;

public class TestMain
{

	public static void main(String[] args)
	{
		Planets pl;
		pl = new Planets();
		float[] fl = new float[2];
		fl[0] = 0.0f;
		fl[1] = 0.0f;

		Planets.addPlanet(new Planet(fl, 1f, 1f));
		fl = new float[2];
		fl[0] = 4.0f;
		fl[1] = 3.0f;

		Planets.addPlanet(new Planet(fl, 1f, 2f));
		InteractionMatrix im = new InteractionMatrix(2);
		ComputeMutualAcceleration cf = new ComputeMutualAcceleration(0, 1, im);
		cf.run();
		System.out.println(pl.toString());
		System.out.println("-------------------------");
		System.out.println(im.toString());
	}
}
