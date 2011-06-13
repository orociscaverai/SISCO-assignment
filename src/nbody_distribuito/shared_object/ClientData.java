package nbody_distribuito.shared_object;

import java.io.Serializable;
/**
 * Classe di passaggio di informazioni tra ComputeActor e Workers
 * contiene informazioni su id posizione e massa del corpo
 * @author Boccacci Andrea, Cicora Saverio
 *
 */
public class ClientData implements Serializable {

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

    /**
     * 
     * @return l'indice del corpo rappresentato nel job
     */
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