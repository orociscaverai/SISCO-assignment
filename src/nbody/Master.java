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
    private ExecutorService executor;
    private InteractionMatrix interactionMatrix;
    private PlanetsMap map;
    private NBodyView view;
    private int numBodies;
    private ArrayBlockingQueue<PlanetsMap> coda;
    private int poolSize;
    private float deltaTime;
    private float softFactor;

    public Master(NBodyView view, ArrayBlockingQueue<PlanetsMap> coda) {
	super("Master");

	this.poolSize = Runtime.getRuntime().availableProcessors() * 3;
	this.deltaTime = Float.parseFloat(System.getProperty("deltaTime",
		"0.005"));
	this.view = view;
	this.coda = coda;
	this.numBodies = 0;
	this.softFactor = 1f;

	view.register(this);

	log(" " + poolSize);
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

	// preset();

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
		Event ev;
		if (!processing) {
		    ev = fetchEvent();
		} else {
		    // processing = true
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

		    } else if (ev.getDescription().equals("singleStep")) {
			processing = false;

		    } else if (ev.getDescription().equals("deltaTime")) {
			this.deltaTime = ((DeltaTimeEvent) ev).getDeltaTime();
			this.softFactor = ((DeltaTimeEvent) ev).getSoftFactor();
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

    float normalize(float[] vector) {
	float dist = (float) Math.sqrt((vector[0] * vector[0] + vector[1]
		* vector[1]));
	if (dist > 1e-6) {
	    vector[0] /= dist;
	    vector[1] /= dist;
	}
	return dist;
    }

    float dot(float[] v0, float[] v1) {
	return v0[0] * v1[0] + v0[1] * v1[1];
    }

}
