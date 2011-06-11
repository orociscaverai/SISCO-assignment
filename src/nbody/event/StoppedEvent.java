package nbody.event;

import nbody.view.swing.NBodyView;

public class StoppedEvent extends Event {

    public StoppedEvent(NBodyView source) {
	super("stopped", source);
    }

}
