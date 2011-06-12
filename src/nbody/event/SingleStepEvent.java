package nbody.event;

import nbody.view.AbstractView;

public class SingleStepEvent extends Event {

    public SingleStepEvent(AbstractView source) {
	super("singleStep", source);
    }

}
