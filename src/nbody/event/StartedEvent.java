package nbody.event;

import gui.NBodyView;

public class StartedEvent extends Event {

    public StartedEvent(NBodyView source) {
	super("started", source);
    }
}
