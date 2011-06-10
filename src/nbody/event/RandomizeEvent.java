package nbody.event;

import nbody.gui.NBodyView;

public class RandomizeEvent extends Event {
    private int numBodies;

    public RandomizeEvent(NBodyView source, int numBodies) {
	super("randomize", source);
	this.numBodies = numBodies;
    }

    public int getNumBodies() {
	return numBodies;
    }
}
