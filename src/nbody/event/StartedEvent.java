package nbody.event;

import nbody.gui.NBodyView;

public class StartedEvent extends Event {

    public StartedEvent(NBodyView source) {
	super("started", source);
    }
}
