package nbody;

import gui.NBodyView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nbody.event.Event;
import nbody.event.ParameterEvent;
import nbody.event.RandomizeEvent;

public class Master extends ControllerAgent {
    private ExecutorService executor;
    private InteractionMatrix interactionMatrix;
    private PlanetsMap map;
    private NBodyView view;
    private int numBodies;
    private int poolSize;
    private float deltaTime;
    private float softFactor;

    public Master(NBodyView view) {
	super("Master");

	this.poolSize = Runtime.getRuntime().availableProcessors() * 3;
	this.deltaTime = Float.parseFloat(System
		.getProperty("deltaTime", "0.5"));
	this.view = view;
	this.numBodies = 0;
	this.softFactor = 1f;

	view.register(this);
	view.setParameter(deltaTime, softFactor);
	log("Pool size: " + poolSize);
    }

    private void doCompute() throws InterruptedException {

	executor = Executors.newFixedThreadPool(poolSize);

	// Combinazioni senza ripetizioni di tutti i Bodies
	// int numTask = (numBodies * (numBodies - 1) * (numBodies - 2)) / 2;
	for (int i = 0; i < numBodies; i++) {
	    for (int j = i + 1; j < numBodies; j++) {
		try {
		    executor.execute(new ComputeMutualAcceleration(i, j,
			    interactionMatrix, map, softFactor));
		    // log("submitted task " + i + " " + j);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}

	PlanetsMap newMap = new PlanetsMap(numBodies);
	executor.shutdown();
	executor.awaitTermination(3600, TimeUnit.SECONDS);
	executor = Executors.newFixedThreadPool(poolSize);

	for (int i = 0; i < numBodies; i++) {

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
	view.setUpdated(newMap);
	this.map = newMap;

    } // doCompute()

    private void doReset() {
	this.numBodies = 0;
	doRandomize();
    }

    private void doRandomize() {
	this.map = new PlanetsMap(numBodies);
	Planets.getInstance().makeRandomBodies(numBodies);

	map.generateRandomMap();
	view.setUpdated(map);

	log("\n" + map.toString());
	log("\n" + Planets.getInstance().toString());

    }// doRandomize()

    public void run() {
	boolean processing = false;

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

		    if (ev.getDescription().equals("started")) {
			processing = true;
			interactionMatrix = new InteractionMatrix(numBodies);

		    } else if (ev.getDescription().equals("randomize")) {
			this.numBodies = ((RandomizeEvent) ev).getNumBodies();
			doRandomize();

		    }
		    if (ev.getDescription().equals("paused")) {
			processing = false;

		    } else if (ev.getDescription().equals("stopped")) {
			processing = false;
			// TODO controllare la terminazione: vengono mandati
			// alla view sempre 2 model dopo la pressione del
			// Pulsante STOP. Provare con 2000 corpi ed usare la
			// stampa PIPPO in NBodyPanel
			doReset();

		    } else if (ev.getDescription().equals("singleStep")) {
			// Effettua una sola computazione
			doCompute();

		    } else if (ev.getDescription().equals("deltaTime")) {
			this.deltaTime = ((ParameterEvent) ev).getDeltaTime();
			this.softFactor = ((ParameterEvent) ev).getSoftFactor();
			log("\nDelta " + deltaTime + "\nSoft " + softFactor);
		    }
		} else {
		    doCompute();
		}
	    }

	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

}
