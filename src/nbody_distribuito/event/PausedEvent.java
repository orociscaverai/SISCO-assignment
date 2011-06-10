package nbody_distribuito.event;

import nbody_distribuito.view.NBodyView;

public class PausedEvent extends Event {

    public PausedEvent(NBodyView source) {
	super("paused", source);
    }

}
