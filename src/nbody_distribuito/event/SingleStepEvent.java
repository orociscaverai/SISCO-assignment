package nbody_distribuito.event;

import nbody_distribuito.view.AbstractView;

public class SingleStepEvent extends Event {

    public SingleStepEvent(AbstractView source) {
	super("singleStep", source);
    }

}
