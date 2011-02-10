package nbody;

import gui.NBodyView;

public class StoppedEvent extends Event {

    public StoppedEvent(NBodyView source) {
	super("stopped", source);
    }

}
