package nbody;

import gui.NBodyView;

/** XXX questa classe è ok */
public class StartedEvent extends Event {

    private int numBodies;

    public StartedEvent(int numBodies, NBodyView source) {
	super("started", source);
	this.numBodies = numBodies;
    }

    public int getNumBodies() {
	return numBodies;
    }
}
