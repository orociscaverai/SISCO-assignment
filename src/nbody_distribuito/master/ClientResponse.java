package nbody_distribuito.master;

import java.io.Serializable;

public class ClientResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private float[] partialVelocity;
    private float[] partialDisplacement;

    public ClientResponse(int id, float[] partialVelocity, float[] partialDisplacement) {
	this.id = id;
	this.partialVelocity = partialVelocity;
	this.partialDisplacement = partialDisplacement;
    }

    public int getBodyId() {
	return id;
    }

    public float[] getPartialVelocity() {
	return partialVelocity;
    }

    public float[] getPartialDisplacement() {
	return partialDisplacement;
    }

    public String toString() {
	String out = "";

	out += "ID: " + id;
	out += "\tpartial velocity: ";
	out += partialVelocity[0];
	out += " ";
	out += partialVelocity[1];
	out += "\tpartialDisplacement: ";
	out += partialDisplacement[0];
	out += " ";
	out += partialDisplacement[1];

	return out;
    }

}
