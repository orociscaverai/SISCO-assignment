package nbody_distribuito.event;

import nbody_distribuito.view.AbstractView;

public class StartedEvent extends Event {

    public StartedEvent(AbstractView source) {
	super("started", source);
    }
}
