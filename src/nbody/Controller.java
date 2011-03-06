package nbody;

import nbody.event.Event;
import nbody.event.ParameterEvent;
import nbody.event.RandomizeEvent;

import gui.NBodyView;

public class Controller extends ControllerAgent{
	private StateMonitor state;
	private StateVariables var;
	private NBodyView view;
	private long timeStep;

	public Controller(NBodyView view, StateMonitor state, StateVariables var) {
		super("Controller");
		this.view = view;
		float deltaTime = Float.parseFloat(System
				.getProperty("deltaTime", "0.5"));
		float softFactor = 1f;
		view.register(this);
		view.setParameter(deltaTime, softFactor);
		this.state = state;
		this.var = var;
		timeStep = 10;

	}

	public void run(){
		long nextComputeTime;
		nextComputeTime = System.currentTimeMillis();
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
						nextComputeTime = System.currentTimeMillis();
					} else if (ev.getDescription().equals("paused")) {
						state.pauseProcess();
					} else if (ev.getDescription().equals("stopped")) {
						log("Sending stop"+ System.currentTimeMillis());
						state.stopProcess();
						//stopFlag.set();
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
						nextComputeTime += timeStep;
					}
				}

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
