package nbody_distribuito.master;

import java.io.Serializable;

public class ClientResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private float[] partialVelocity;
	private float[] partialDisplacement;

	public ClientResponse(int id, float[] partialVelocity,
			float[] partialDisplacement) {
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
		out += "ID: " + id + "\tpartial velocity: " + partialVelocity[0] + " "
				+ partialVelocity[1] + "\tpartialDisplacement: "
				+ partialDisplacement[0] + " " + partialDisplacement[1];
		return out;
	}

}
