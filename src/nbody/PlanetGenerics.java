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

	for (int d = 0; d < dimension; d++)
	    this.velocity[d] = velocity[d];
    }

    public float[] getVelocity() {
	return velocity;
    }

    public void setVelocity(float[] velocity) {
	this.velocity = velocity;
    }

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
	String out = "Velocity:";

	for (int d = 0; d < dimension; d++) {
	    out += " " + velocity[d];
	}

	out += "\n";

	out += "Mass: " + mass + "\n";
	return out;
    }

}