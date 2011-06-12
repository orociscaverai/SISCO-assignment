package nbody.event;

import nbody.view.AbstractView;

public class StartedEvent extends Event {

    public StartedEvent(AbstractView source) {
	super("started", source);
    }
}
