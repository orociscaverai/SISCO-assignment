package nbody;

import nbody.event.Event;
import nbody.event.ParameterEvent;
import nbody.event.RandomizeEvent;

import gui.NBodyView;

public class Controller extends ControllerAgent{
	private StateMonitor state;
	private StateVariables var;
	private Master m;
	private NBodyView view;
	private long timeStep;

	public Controller(NBodyView view) {
		super("Controller");
		this.view = view;
		float deltaTime = Float.parseFloat(System
				.getProperty("deltaTime", "0.5"));
		float softFactor = 1f;
		view.register(this);
		view.setParameter(deltaTime, softFactor);
		state = new StateMonitor();
		var = new StateVariables();
		timeStep = 10;

	}

	public void run(){
		m = new Master(state,var);
		m.start();
		long nextComputeTime;
		nextComputeTime = System.currentTimeMillis() + timeStep;
		try {
			while (true) {
				Event ev;
				if (state.isSuspended()) {
					ev = fetchEvent();
				} else {
					// processing == true
					ev = fetchEventIfPresent();
				}

				if (ev != null) {
					log("received ev: " + ev.getDescription());

					if (ev.getDescription().equals("randomize")){
						RandomizeEvent rev = (RandomizeEvent)ev;
						var.clearPendingMaps();
						var.setNumBodies(rev.getNumBodies());
						state.notifyRandomize();
						view.setUpdated(var.getMap());
					}

					if (ev.getDescription().equals("started")) {
						state.startProcess();
					} else if (ev.getDescription().equals("paused")) {
						state.pauseProcess();
					} else if (ev.getDescription().equals("stopped")) {
						state.stopProcess();
						m.interrupt();
						var.clearPendingMaps();

					} else if (ev.getDescription().equals("singleStep")) {
						// Effettua una sola computazione
						state.step();
						view.setUpdated(var.getMap());

					} else if (ev.getDescription().equals("deltaTime")) {
						//TODO migliorare questa routine
						var.setDeltaTime(((ParameterEvent) ev).getDeltaTime());
						var.setSoftFactor(((ParameterEvent) ev).getSoftFactor());
						//log("\nDelta " + deltaTime + "\nSoft " + softFactor);
					}
				}else{
					if(System.currentTimeMillis()> nextComputeTime){
						view.setUpdated(var.getMap());
						nextComputeTime = System.currentTimeMillis() + timeStep;
					}
				}

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
