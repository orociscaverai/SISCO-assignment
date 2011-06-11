package nbody.event;

import nbody.view.swing.NBodyView;

public class StartedEvent extends Event {

    public StartedEvent(NBodyView source) {
	super("started", source);
    }
}
