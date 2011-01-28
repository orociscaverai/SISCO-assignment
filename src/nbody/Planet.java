package nbody;

public class Planet
{

	final private static int dimension = 2;
	private float[] position = new float[dimension];
	private float[] velocity = new float[dimension];
	private float[] acceleration = new float[dimension];
	private float mass;
	private float radius;

	public Planet(float[] position, float radius, float mass) {
		this.position = position;
		this.radius = radius;
		this.mass = mass;
	}

	/**
	 * @return the position
	 */
	public float[] getPosition()
	{
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(float[] position)
	{
		this.position = position;
	}

	/**
	 * @return the velocity
	 */
	public float[] getVelocity()
	{
		return velocity;
	}

	/**
	 * @param velocity
	 *            the velocity to set
	 */
	public void setVelocity(float[] velocity)
	{
		this.velocity = velocity;
	}

	/**
	 * @return the acceleration
	 */
	public float[] getAcceleration()
	{
		return acceleration;
	}

	/**
	 * @param acceleration
	 *            the acceleration to set
	 */
	public void setAcceleration(float[] acceleration)
	{
		this.acceleration = acceleration;
	}

	/**
	 * @return the radius
	 */
	public float getRadius()
	{
		return radius;
	}

	/**
	 * @param radius
	 *            the radius to set
	 */
	public void setRadius(float radius)
	{
		this.radius = radius;
	}

	/**
	 * @return the radius
	 */
	public float getMass()
	{
		return mass;
	}

	/**
	 * @param radius
	 *            the radius to set
	 */
	public void setMass(float mass)
	{
		this.mass = mass;
	}

	public String toString()
	{
		String out = "";
		out += "Position: " + position[0] + " " + position[1] + "\n";
		out += "Mass: " + mass + "\n";
		out += "Radius: " + radius + "\n";
		return out;
	}
}