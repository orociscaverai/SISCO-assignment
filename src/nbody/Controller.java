package nbody;

import java.util.concurrent.ArrayBlockingQueue;

import nbody.event.Event;
import nbody.event.RandomizeEvent;

import gui.NBodyView;

public class Controller extends ControllerAgent{
	private Master m;
	private NBodyView view;
	private ArrayBlockingQueue<BodiesMap> mapQueue;
	private long timeStep;

	public Controller(NBodyView view) {
		super("Controller");
		this.view = view;
		mapQueue = new ArrayBlockingQueue<BodiesMap>(50, true);
		float deltaTime = Float.parseFloat(System
				.getProperty("deltaTime", "0.5"));
		float softFactor = 1f;
		view.register(this);
		view.setParameter(deltaTime, softFactor);
		timeStep = 10;

	}

	public void run(){
		boolean processing = false;
		long nextComputeTime;
		nextComputeTime = System.currentTimeMillis() + timeStep;
		try {
			while (true) {
				Event ev;
				if (!processing) {
					ev = fetchEvent();
				} else {
					// processing == true
					ev = fetchEventIfPresent();
				}

				if (ev != null) {
					log("received ev: " + ev.getDescription());

					if (ev.getDescription().equals("randomize")){
						RandomizeEvent rev = (RandomizeEvent)ev;
						if (m != null)
							m.interrupt();
						mapQueue.clear();
						m = new Master(rev.getNumBodies(),mapQueue);
						m.start();
						view.setUpdated(mapQueue.take());
					}

					if (ev.getDescription().equals("started")) {
						processing = true;
					}
					if (ev.getDescription().equals("paused")) {
						processing = false;
					} else if (ev.getDescription().equals("stopped")) {
						processing = false;
						m.interrupt();
						mapQueue.clear();

					} else if (ev.getDescription().equals("singleStep")) {
						// Effettua una sola computazione
						view.setUpdated(mapQueue.take());

					} else if (ev.getDescription().equals("deltaTime")) {
						//this.deltaTime = ((ParameterEvent) ev).getDeltaTime();
						//this.softFactor = ((ParameterEvent) ev).getSoftFactor();
						//log("\nDelta " + deltaTime + "\nSoft " + softFactor);
					}
				}else{
					if(System.currentTimeMillis()> nextComputeTime){
						view.setUpdated(mapQueue.take());
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
