package nbody_distribuito.message;

import nbody_distribuito.Constants;
import pcd.actors.Message;

public class RandomizeMessage extends Message {

    private static final long serialVersionUID = 1L;

    private static final String messageType = Constants.RANDOMIZE_EVENT;

    private final int numBodies;

    public RandomizeMessage(int numBodies) {
	super(messageType);
	this.numBodies = numBodies;
    }

    /**
     * @return the numBodies
     */
    public int getNumBodies() {
	return numBodies;
    }

    @Override
    public String toString() {

	String args = getType() + " ";

	args += "numBodies: ";
	args += numBodies;

	return args;

    }

}
