package nbody_distribuito.shared_object;

import java.io.Serializable;

public class Interaction implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private int indexA;
    private int indexB;

    public Interaction(int indexA, int indexB) {
	this.indexA = indexA;
	this.indexB = indexB;
    }

    public int getFirstIndex() {
	return indexA;
    }

    public int getSecondIndex() {
	return indexB;
    }

    public String toString() {
	String out = "";
	out += "first Index :" + indexA + " \tsecond index: " + indexB;
	return out;
    }
}
