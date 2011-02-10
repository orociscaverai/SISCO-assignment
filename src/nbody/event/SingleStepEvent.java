package nbody.event;

import gui.NBodyView;

public class SingleStepEvent extends Event {

    public SingleStepEvent(NBodyView source) {
	super("singleStep", source);
    }

}
