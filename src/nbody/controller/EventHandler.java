package nbody.controller;

import nbody.event.ChangeParamEvent;
import nbody.event.Event;
import nbody.event.OpenFileEvent;
import nbody.event.RandomizeEvent;
import nbody.event.SingleStepEvent;
import nbody.event.StartedEvent;
import nbody.event.StoppedEvent;
import nbody.event.PausedEvent;
import nbody.model.StateMonitor;
import nbody.model.StateVariables;
import nbody.view.AbstractView;

public class EventHandler extends ControllerAgent {
    private StateMonitor state;
    private StateVariables var;

    public EventHandler(AbstractView view, StateMonitor state, StateVariables var) {
	super("Controller");
	float deltaTime = 0.5f;
	float softFactor = 0.6f;
	view.register(this);
	view.setParameter(deltaTime, softFactor);
	this.state = state;
	this.var = var;

    }

    public void run() {
	try {
	    while (true) {
		Event ev;
		ev = fetchEvent();
		if (ev instanceof RandomizeEvent) {
		    log("received ev: " + ev.getDescription());
		    RandomizeEvent rev = (RandomizeEvent) ev;
		    var.setNumBodies(rev.getNumBodies());
		    state.notifyRandomize();
		} else if (ev instanceof StartedEvent) {
		    log("received ev: " + ev.getDescription());
		    state.startProcess();
		} else if (ev instanceof PausedEvent) {
		    log("received ev: " + ev.getDescription());
		    state.pauseProcess();
		} else if (ev instanceof StoppedEvent) {
		    log("received ev: " + ev.getDescription());
		    state.stopProcess();
		} else if (ev instanceof SingleStepEvent) {
		    log("received ev: " + ev.getDescription());
		    state.stepProcess();
		} else if (ev instanceof ChangeParamEvent) {
		    log("received ev: " + ev.getDescription());
		    ChangeParamEvent cp = ((ChangeParamEvent) ev);
		    var.setDeltaTime(cp.getDeltaTime());
		    var.setSoftFactor(cp.getSoftFactor());
		}else if (ev instanceof OpenFileEvent){
		    log("received ev: " + ev.getDescription());
			var.setFile(((OpenFileEvent) ev).getFile());
			state.notifyOpenFile();
		}
	    }

	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }
}
