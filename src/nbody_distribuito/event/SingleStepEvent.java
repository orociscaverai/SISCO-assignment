package nbody_distribuito.event;

import nbody_distribuito.view.NBodyView;

public class SingleStepEvent extends Event {

    public SingleStepEvent(NBodyView source) {
	super("singleStep", source);
    }

}
