package nbody;

import gui.NBodyView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Master extends ControllerAgent {
    private int deltaTime;
    private ExecutorService executor;
    private InteractionMatrix interactionMatrix;
    private int poolSize;
    private PlanetsMap map;
    private NBodyView view;
    private int numBodies;

    public Master(NBodyView view) {
	super("Pippo");
	this.poolSize = Integer.parseInt(System.getProperty("poolSize", "6"));
	this.deltaTime = Integer.parseInt(System.getProperty("deltaTime", "1"));
	this.view = view;

	view.register(this);
    }

    private void doCompute(int numBodies) throws InterruptedException {
	Planets.getInstance().makeRandomBodies(numBodies);
	interactionMatrix = new InteractionMatrix(numBodies);
	this.map = new PlanetsMap(numBodies);

	executor = Executors.newFixedThreadPool(poolSize);
	long time = System.currentTimeMillis();

	// double step = (b - a) / numTasks;
	// Combinazioni senza ripetizioni di tutti i Bodies
	// Utile come debug
	// int numTask = (numBodies * (numBodies - 1) * (numBodies - 2)) / 2;
	for (int i = 0; i < numBodies - 1; i++) {
	    for (int j = i + 1; j < numBodies; j++) {
		try {
		    executor.execute(new ComputeMutualAcceleration(i, j,
			    interactionMatrix, map));
		    // log("submitted task " + i + " " + j);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}

	PlanetsMap newMap = new PlanetsMap(numBodies);
	executor.shutdown();
	executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
	// / XXX Stampa per Debug
	System.out.println(System.currentTimeMillis() - time);
	// System.out.print(interactionMatrix.toString());
	executor = Executors.newFixedThreadPool(poolSize);
	System.out.println(System.currentTimeMillis() - time);

	for (int i = 0; i < numBodies - 1; i++) {

	    try {
		executor.execute(new ComputeNewPosition(i, map.getPosition(i),
			deltaTime, interactionMatrix, newMap));
		// log("submitted task " + i + " " + j);
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	}

	executor.shutdown();
	executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
	System.out.println(System.currentTimeMillis() - time);

    } // doCompute()

    public void run() {
	boolean processing = false;
	try {
	    while (true) {
		if (!processing) {
		    Event ev = fetchEvent();
		    // log("received ev: "+ev.getDescription());
		    if (ev.getDescription().equals("started") && !processing) {
			StartedEvent evs = (StartedEvent) ev;
			processing = true;
			this.numBodies = evs.getNumBodies();
			Planets.getInstance().makeRandomBodies(numBodies);
			interactionMatrix = new InteractionMatrix(numBodies);
			this.map = new PlanetsMap(numBodies);
			map.GenerateRandomMap();
		    }
		} else { // processing = true
		    Event ev = fetchEventIfPresent();
		    if (ev != null) {
			if (ev.getDescription().equals("pause")) {
			    // doDisplayTaskCompletion();
			    processing = false;
			} else if (ev.getDescription().equals("stopped")) {
			    // doDisplayTaskInterruption();
			    processing = false;
			}
		    } else {
			doCompute(numBodies);
		    }
		}
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}
