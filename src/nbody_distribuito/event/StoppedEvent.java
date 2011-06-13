package nbody_distribuito.event;

import nbody_distribuito.view.AbstractView;

public class StoppedEvent extends Event {

    public StoppedEvent(AbstractView source) {
	super("stopped", source);
    }

}
