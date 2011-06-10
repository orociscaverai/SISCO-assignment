package nbody.event;

import nbody.gui.NBodyView;

public class SingleStepEvent extends Event {

    public SingleStepEvent(NBodyView source) {
	super("singleStep", source);
    }

}
