package nbody;

public class TestMain
{

	public static void main(String[] args)
	{
		PlanetsMap pl;
		pl = new PlanetsMap();
		float[] fl = new float[2];
		fl[0] = 0.0f;
		fl[1] = 0.0f;

		pl.addPlanet(new Planet(fl, 1f, 1f));
		fl = new float[2];
		fl[0] = 4.0f;
		fl[1] = 3.0f;

		pl.addPlanet(new Planet(fl, 1f, 2f));
		InteractionMatrix im = new InteractionMatrix(2);
		ComputeMutualAcceleration cf = new ComputeMutualAcceleration(0, 1, im,pl);
		cf.run();
		System.out.println(pl.toString());
		System.out.println("-------------------------");
		System.out.println(im.toString());
		PlanetsMap newMap = new PlanetsMap();
		ComputeNewPosition cp1 = new ComputeNewPosition(0, 2, im,pl,newMap);
		ComputeNewPosition cp2 = new ComputeNewPosition(1, 1, im,pl,newMap);
		cp1.run();
		cp2.run();
		System.out.println(newMap.toString());
		System.out.println("-------------------------");
	}
}
