package nbody_distribuito.event;

import nbody_distribuito.view.AbstractView;

public class RandomizeEvent extends Event {
    private int numBodies;

    public RandomizeEvent(AbstractView source, int numBodies) {
	super("randomize", source);
	this.numBodies = numBodies;
    }

    public int getNumBodies() {
	return numBodies;
    }
}
