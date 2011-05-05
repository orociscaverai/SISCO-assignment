package nbody;

import gui.NBodyView;
import nbody.event.Event;
import nbody.event.ChangeParamEvent;
import nbody.event.RandomizeEvent;

public class EventHandler extends ControllerAgent {
    private StateMonitor state;
    private StateVariables var;
    private NBodyView view;

    public EventHandler(NBodyView view, StateMonitor state, StateVariables var) {
	super("Controller");
	this.view = view;
	float deltaTime = Float.parseFloat(System.getProperty("deltaTime", "0.5"));
	float softFactor = 1f;
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
		log("received ev: " + ev.getDescription());
		if (ev.getDescription().equals("randomize")) {
		    RandomizeEvent rev = (RandomizeEvent) ev;
		    var.setNumBodies(rev.getNumBodies());
		    // TODO
		    state.notifyRandomize();
		}
		// FIXME ci vuole l'else?
		if (ev.getDescription().equals("started")) {
		    state.startProcess();
		} else if (ev.getDescription().equals("paused")) {
		    state.pauseProcess();
		} else if (ev.getDescription().equals("stopped")) {
		    log("Sending stop " + System.currentTimeMillis());
		    state.stopProcess();
		} else if (ev.getDescription().equals("singleStep")) {
		    // Effettua una sola computazione
		    state.stepProcess();

		    // TODO chiamare l'evento parameter o qualcosa di simile
		} else if (ev.getDescription().equals("changeParam")) {
		    // TODO migliorare questa routine
		    var.setDeltaTime(((ChangeParamEvent) ev).getDeltaTime());
		    var.setSoftFactor(((ChangeParamEvent) ev).getSoftFactor());
		    // log("\nDelta " + deltaTime + "\nSoft " + softFactor);
		}
	    }

	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
