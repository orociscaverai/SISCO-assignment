package nbody;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PlanetsMap
{
	private List<Planet> planets;
	final private static int dimension = 2;

	// TODO le liste hanno un numero massimo di lementi contenibile che è uguale a Integer.MAX_VALUE

	/** Costruisce una lista di pianeti vuota */
	public PlanetsMap() {
		this(0);
	}

	/**
	 * Costruisce una lista con numBodies elementi generati in maniera casuale
	 */
	public PlanetsMap(int numBodies) {
		planets = new LinkedList<Planet>();
		Random rand = new Random();
		float radius;
		for (int i = 0; i < numBodies; i++)
		{
			float position[] = new float[dimension];
			for (int k = 0; k < dimension; k++)
			{
				position[k] = rand.nextFloat();
			}
			// XXX la proporzione la considerimo 1/20
			// TODO considerare un minimo
			radius = rand.nextFloat() / 20;
			// calcolare la massa della Terra aggiungere un fattore casuale per la densità
			planets.add(new Planet(position, radius, radius * 100));
		}
	}

	public Planet getPlanet(int index)
	{
		return planets.get(index);
	}

	public void addPlanet(Planet p)
	{
		planets.add(p);
	}

	public int getNumBodies()
	{
		return planets.size();
	}

	public String toString()
	{
		String out = "";
		int i = 0;
		for (Planet p : planets)
		{
			out += "Planet " + i + "\n";
			out += p.toString() + "\n";
			i++;
		}
		return out;
	}
}
