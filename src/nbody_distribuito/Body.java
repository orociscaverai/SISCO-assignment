package nbody_distribuito;

public class Body {

    final private static int dimension = 2;

    private int id;
    private float mass;
    private float[] velocity;
    private float[] position;

    public Body(int id, float mass) {
	this(id, mass, new float[dimension]);
    }

    public Body(int id, float mass, float[] position) {
	this.id = id;
	this.mass = mass;
	this.position = new float[dimension];

	for (int d = 0; d < dimension; d++)
	    this.position[d] = position[d];
	
	this.velocity = new float[dimension];
    }

    public int getId() {
	return id;
    }

    public float[] getPosition() {
	return position;
    }

    public void setPosition(float[] pos) {
	this.position = pos;
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

    public String toString() {
	String out = "ID: " + id;

	out += "\tPosition:";

	for (int d = 0; d < dimension; d++) {
	    out += " " + position[d];
	}

	out += "\tVelocity:";

	for (int d = 0; d < dimension; d++) {
	    out += " " + velocity[d];
	}

	out += "\tMass: " + mass;

	return out;
    }

}