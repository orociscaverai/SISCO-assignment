package nbody;

import gui.NBodySetListener;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class FrameRateUpdater extends Thread {

    private ArrayList<NBodySetListener> listeners;
    private StateMonitor state;
    private long timestep = 30;
    private ArrayBlockingQueue<BodiesMap> mapQueue;

    public FrameRateUpdater(StateMonitor state,
	    ArrayBlockingQueue<BodiesMap> mapQueue) {
	this.state = state;
	this.mapQueue = mapQueue;
	listeners = new ArrayList<NBodySetListener>();
    }

    public void run() {
	long nextComputeTime;
	nextComputeTime = System.currentTimeMillis();
	while (true) {
	    try {
		long ready = nextComputeTime - System.currentTimeMillis();
		if (!state.isSuspended()) {
		    if (ready > 0) {
			log("sleep for " + ready + " ms");
			Thread.sleep(ready);
		    }
		} else {
			log("Attendo Update");
		    state.WaitUpdate();
		    log("sblocco Update");
		    if (!state.isStopped())
		    	nextComputeTime = System.currentTimeMillis();
		    else
		    	mapQueue.clear();
		}
		notifyListeners();
		nextComputeTime += timestep;
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

    public void addListener(NBodySetListener l) {
	listeners.add(l);
    }

    private void notifyListeners() throws InterruptedException {
	for (NBodySetListener l : listeners) {
	    l.setUpdated(mapQueue.take());
	}
    }

    private void log(String log) {
	System.out.println("[FPS Updater] " + log);
    }
}
