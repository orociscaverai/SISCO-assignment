package nbody.event;

import nbody.view.AbstractView;

public class StoppedEvent extends Event {

    public StoppedEvent(AbstractView source) {
	super("stopped", source);
    }

}
