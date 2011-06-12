package nbody.event;

import nbody.view.AbstractView;

public class PausedEvent extends Event {

    public PausedEvent(AbstractView source) {
	super("paused", source);
    }

}
