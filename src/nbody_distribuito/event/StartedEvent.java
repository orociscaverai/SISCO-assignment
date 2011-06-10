package nbody_distribuito.event;

import nbody_distribuito.view.NBodyView;

public class StartedEvent extends Event {

    public StartedEvent(NBodyView source) {
	super("started", source);
    }
}
