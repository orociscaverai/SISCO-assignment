package nbody;

import gui.NBodyView;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nbody.event.DeltaTimeEvent;
import nbody.event.Event;
import nbody.event.RandomizeEvent;

public class Master extends ControllerAgent {
    private float deltaTime;
    private ExecutorService executor;
    private InteractionMatrix interactionMatrix;
    private int poolSize;
    private PlanetsMap map;
    private NBodyView view;
    private int numBodies;
    private ArrayBlockingQueue<PlanetsMap> coda;

    public Master(NBodyView view, ArrayBlockingQueue<PlanetsMap> coda) {
	super("Master");
	this.poolSize = Integer.parseInt(System.getProperty("poolSize", "6"));
	this.deltaTime = Float.parseFloat(System.getProperty("deltaTime",
		"0.005"));
	this.view = view;
	this.coda = coda;
	this.numBodies = 0;

	view.register(this);
    }

    private void doCompute(int numBodies) throws InterruptedException {

	executor = Executors.newFixedThreadPool(poolSize);

	// double step = (b - a) / numTasks;
	// Combinazioni senza ripetizioni di tutti i Bodies
	// Utile come debug
	// int numTask = (numBodies * (numBodies - 1) * (numBodies - 2)) / 2;
	for (int i = 0; i < numBodies -1; i++) {
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
	executor.awaitTermination(3600, TimeUnit.SECONDS);
	// / XXX Stampa per Debug
	// System.out.print(interactionMatrix.toString());
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
	coda.add(newMap);
	view.setUpdated(newMap);
	this.map = newMap;

    } // doCompute()

    private void doRandomize() {
	this.map = new PlanetsMap(numBodies);
	Planets.getInstance().makeRandomBodies(numBodies);

	map.generateRandomMap();
	coda.add(map);
	view.setUpdated(null);

	log(map.toString());
	log(Planets.getInstance().toString());
	
//	preset();

    }// doRandomize()

    public void preset() {
	this.map = new PlanetsMap(3);
	Planets p = Planets.getInstance();
	p.addPlanet(new PlanetGenerics(0.2f));
	p.addPlanet(new PlanetGenerics(0.01f));
	p.addPlanet(new PlanetGenerics(0.000001f));

	float[] pos = new float[2];
	pos[0] = 0;
	pos[1] = 0;
	map.setPosition(0, pos);

	pos = new float[2];
	pos[0] = 0.16f;
	pos[1] = 0;
	map.setPosition(1, pos);

	pos = new float[2];
	pos[0] = 0.14f;
	pos[1] = 0;
	map.setPosition(2, pos);
	// //////////////////////////////////////////////
	pos = new float[2];
	pos[0] = 0;
	pos[1] = 0;
	p.getPlanet(0).setVelocity(pos);

	pos = new float[2];
	pos[0] = 0;
	pos[1] = 0.12f;
	p.getPlanet(1).setVelocity(pos);

	pos = new float[2];
	pos[0] = 0;
	pos[1] = 0.053f;
	p.getPlanet(2).setVelocity(pos);

	coda.add(map);
	view.setUpdated(null);

	log(map.toString());
	log(Planets.getInstance().toString());
    }

    public void run() {
	boolean processing = false;
	try {
	    while (true) {
		if (!processing) {
		    Event ev = fetchEvent();
		    log("received ev: " + ev.getDescription());

		    if (ev.getDescription().equals("started")) {
			processing = true;
			interactionMatrix = new InteractionMatrix(numBodies);

		    } else if (ev.getDescription().equals("randomize")) {
			this.numBodies = ((RandomizeEvent) ev).getNumBodies();
			doRandomize();

		    } else if (ev.getDescription().equals("deltaTime")) {
			this.deltaTime = ((DeltaTimeEvent) ev).getDeltaTime();
			log("Delta" + deltaTime);
		    }
		} else { // processing = true
		    Event ev = fetchEventIfPresent();
		    if (ev != null) {
			if (ev.getDescription().equals("paused")) {
			    processing = false;

			} else if (ev.getDescription().equals("stopped")) {
			    processing = false;

			} else if (ev.getDescription().equals("singleStep")) {
			    processing = false;

			} else if (ev.getDescription().equals("deltaTime")) {
			    this.deltaTime = ((DeltaTimeEvent) ev)
				    .getDeltaTime();
			    log("Delta" + deltaTime);
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
