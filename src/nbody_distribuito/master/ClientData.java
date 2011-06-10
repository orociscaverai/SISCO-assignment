package nbody_distribuito.master;

import java.io.Serializable;

public class ClientData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int id, idRelative;
    private float[] pos = new float[2];
    private float mass;

    public ClientData(int idRelative, int id, float pos[], float mass) {
	this.idRelative = idRelative;
	this.id = id;
	this.pos = pos;
	this.mass = mass;
    }

    public int getRelativeId() {
	return idRelative;
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

	out += "RelativeIndex: " + idRelative;
	out += " ID: " + id;
	out += "\t pos: " + pos[0] + " : " + pos[1];
	out += "\tmass: " + mass;

	return out;

    }

}