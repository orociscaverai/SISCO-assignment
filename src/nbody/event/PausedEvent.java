package nbody.event;

import nbody.gui.NBodyView;

public class PausedEvent extends Event {

    public PausedEvent(NBodyView source) {
	super("paused", source);
    }

}
