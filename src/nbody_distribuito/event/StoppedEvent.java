package nbody_distribuito.event;

import nbody_distribuito.view.NBodyView;

public class StoppedEvent extends Event {

    public StoppedEvent(NBodyView source) {
	super("stopped", source);
    }

}
