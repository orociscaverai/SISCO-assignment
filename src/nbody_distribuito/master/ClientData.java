package nbody_distribuito.master;

import java.io.Serializable;

public class ClientData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private float[] pos = new float[2];
    private float mass;

    public ClientData(int id, float pos[], float mass) {

	this.id = id;
	this.pos = pos;
	this.mass = mass;
    }

    /**
     * @return the id
     */
    public int getId() {
	return id;
    }

    /**
     * @return the pos
     */
    public float[] getPos() {
	return pos;
    }

    /**
     * @return the mass
     */
    public float getMass() {
	return mass;
    }

    public String toString() {
	String out = "";

	out += "ID: " + id + "\t pos: " + pos[0] + " : " + pos[1] + "\tmass: " + mass;

	return out;

    }

}