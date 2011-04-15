package nbody;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Master extends Thread {
    private InteractionMatrix interactionMatrix;
    private BodiesMap map;
    private int poolSize;
    private int numBodies;
    private StateMonitor state;
    private StateVariables var;
    private ArrayBlockingQueue<BodiesMap> mapQueue;

    private ExecutorService ex;
    private ExecutorCompletionService<Boolean> compServ;

    public Master(StateMonitor state, StateVariables var,
	    ArrayBlockingQueue<BodiesMap> mapQueue) {
	super("Master");

	this.poolSize = Runtime.getRuntime().availableProcessors() * 3;

	this.var = var;
	this.state = state;
	this.mapQueue = mapQueue;

    }

    private void initPool() {
	ex = Executors.newFixedThreadPool(poolSize);
	this.compServ = new ExecutorCompletionService<Boolean>(ex);
    }

    private void shutdownAndReset() throws InterruptedException {
	ex.shutdownNow();
	ex.awaitTermination(2, TimeUnit.MINUTES);
	int debug = 0;
	while (compServ.poll() != null) {
	    debug++;
	}
	log("Numero di risultati già terminati: " + debug);

	initPool();

	doReset();
    }

    private void doCompute() throws InterruptedException {

	int numBodies = var.getNumBodies();
	float deltaTime = var.getDeltaTime();
	float softFactor = var.getSoftFactor();
	interactionMatrix = new InteractionMatrix(numBodies);

	// Inizio la fase 1
	try {
	    for (int i = 0; i < numBodies - 1; i++) {
		for (int j = i + 1; j < numBodies; j++) {
		    compServ.submit(new ComputeMutualAcceleration(i, j,
			    interactionMatrix, map, softFactor));
		    // log("submitted task " + i + " " + j);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

	// Combinazioni senza ripetizioni di tutti i Bodies
	int numTask = (numBodies * (numBodies - 1)) / 2;

	// La creo qui per guadagnare tempo
	BodiesMap newMap = new BodiesMap(numBodies);
	try {
	    for (int n = 0; n < numTask; n++) {
		compServ.take();
		if (state.isStopped()) {
		    log("Stop alla Fase 1");
		    shutdownAndReset();
		    return;
		}
	    }
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    // Mi assicuro di tornare ad uno stato consistente
	    shutdownAndReset();
	    return;
	}

	// Inizio la fase 2
	for (int i = 0; i < numBodies; i++) {

	    compServ.submit(new ComputeNewPosition(i, map.getPosition(i),
		    deltaTime, interactionMatrix, newMap));
	    // log("submitted task " + i + " " + j);
	}

	try {
	    for (int n = 0; n < numBodies; n++) {

		compServ.take();
		if (state.isStopped()) {
		    log("Stop alla Fase 2");
		    shutdownAndReset();
		    return;
		}
	    }
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    // Mi assicuro di tornare ad uno stato consistente
	    shutdownAndReset();
	    return;
	}

	// Posso usarla anche se l'ho inviata alla vista, dato che d'ora
	// in poi verrà acceduta solo in lettura
	map = newMap;

	mapQueue.put(newMap);

    }// doCompute()

    private void doReset() throws InterruptedException {
	var.setNumBodies(0);
	map = new BodiesMap(0);
	interactionMatrix = new InteractionMatrix(0);
    }

    private void doRandomize() throws InterruptedException {
	numBodies = var.getNumBodies();
	this.map = new BodiesMap(numBodies);
	interactionMatrix = new InteractionMatrix(numBodies);
	Bodies.getInstance().makeRandomBodies(numBodies);

	map.generateRandomMap();
	System.out.println("messo");
	mapQueue.put(map);

	// log("\n" + map.toString());
	// log("\n" + Bodies.getInstance().toString());

    }// doRandomize()

    public void run() {
    	initPool();
	while (true) {
	    try {
		while (true) {
			state.waitAction();
		    if(!state.isStopped()){
		    	doCompute();
		    	log("do Compute");
		    }
		    else
		    	doRandomize();
		    if (state.isStopped()) {
			log("Stopped " + System.currentTimeMillis());
			mapQueue.clear();
		    }
		}

	    } catch (Exception ex) {
		ex.printStackTrace();
		log("restartingTime " + System.currentTimeMillis());
		break;
	    }
	}
    }

    private void log(String error) {
	System.out.println("[MASTER] : " + error);

    }
}
