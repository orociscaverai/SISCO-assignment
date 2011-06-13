package nbody_distribuito.event;

import nbody_distribuito.view.AbstractView;

public class PausedEvent extends Event {

    public PausedEvent(AbstractView source) {
	super("paused", source);
    }

}
