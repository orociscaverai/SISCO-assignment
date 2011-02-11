package nbody;

public class PlanetGenerics {

    final private static int dimension = 2;
    private float mass;
    private float invMass;
    private float[] velocity;

    public PlanetGenerics(float mass) {
	this(mass, new float[dimension]);
    }

    public PlanetGenerics(float mass, float[] velocity) {
	this.mass = mass;
	this.velocity = new float[dimension];

	for (int i = 0; i < dimension; i++)
	    this.velocity[i] = velocity[i];
    }

    public float[] getVelocity() {
	return velocity;
    }

    /**
     * @param velocity
     *            the velocity to set
     */
    public void setVelocity(float[] velocity) {
	this.velocity = velocity;
    }

    /**
     * @return the acceleration
     */
    /*
     * public float[] getAcceleration() { return acceleration; }
     */
    /**
     * @param acceleration
     *            the acceleration to set
     */
    /*
     * public void setAcceleration(float[] acceleration) { this.acceleration =
     * acceleration; }
     */

    public float getMass() {
	return mass;
    }

    public void setMass(float mass) {
	this.mass = mass;
    }

    /**
     * @return the inverse of mass
     */
    public float getInvMass() {
	return invMass;
    }

    public String toString() {
	String out = "";
	// out += "Acceleration;" + acceleration[0] + " " + acceleration[1] +
	// "\n";
	out += "Velocity: " + velocity[0] + " " + velocity[1] + "\n";
	// out += "Position: " + position[0] + " " + position[1] + "\n";
	out += "Mass: " + mass + "\n";
	return out;
    }

}
