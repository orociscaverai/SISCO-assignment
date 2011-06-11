package nbody.event;

import nbody.view.swing.NBodyView;

public class PausedEvent extends Event {

    public PausedEvent(NBodyView source) {
	super("paused", source);
    }

}
