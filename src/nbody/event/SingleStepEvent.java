package nbody.event;

import nbody.view.swing.NBodyView;

public class SingleStepEvent extends Event {

    public SingleStepEvent(NBodyView source) {
	super("singleStep", source);
    }

}
