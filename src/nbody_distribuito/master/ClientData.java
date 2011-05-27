package nbody_distribuito.master;

class ClientData {

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

}