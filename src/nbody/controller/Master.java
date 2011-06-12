package nbody.controller;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nbody.common.StateMonitor;
import nbody.common.StateVariables;
import nbody.model.Bodies;
import nbody.model.BodiesMap;
import nbody.model.InteractionMatrix;
import nbody.view.AbstractView;

public class Master extends Thread {

    private InteractionMatrix interactionMatrix;
    private BodiesMap map;

    private AbstractView view;
    private StateVariables var;
    private StateMonitor state;

    private ExecutorService ex;
    private ExecutorCompletionService<Boolean> compServ;

    public Master(AbstractView view, StateMonitor state, StateVariables var) {
	super("Master");

	this.view = view;
	this.var = var;
	this.state = state;

	doReset();
    }

    /** Inizializza gli Executor */
    private void initPool() {
	int poolSize = Runtime.getRuntime().availableProcessors() + 1;
	ex = Executors.newFixedThreadPool(poolSize);
	this.compServ = new ExecutorCompletionService<Boolean>(ex);
    }

    /**
     * Funzione utile in caso di stop dell'applicazione: si occupa di attendere
     * l'arresto della computazione e di riportare lapplicazione in uno stato
     * consistente
     */
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

    /**
     * Coordina le fasi necessarie per la computazione di un singolo step della
     * simulazione. Il lavoro viene implementato mediante gli excuto di Java.
     * Infine viene inviata la notifica alla Gui per effettuare l'aggiornamento
     * della visualizzazione.
     */
    private void doCompute() throws InterruptedException {

	int numBodies = map.getNumBodies();
	float deltaTime = var.getDeltaTime();
	float softFactor = var.getSoftFactor();

	// Inizio la fase 1
	try {
	    for (int i = 0; i < numBodies - 1; i++) {
		for (int j = i + 1; j < numBodies; j++) {
		    compServ.submit(new ComputeMutualAcceleration(i, j, interactionMatrix, map,
			    softFactor), true);
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
		    // Per lo stop viene usata la classe
		    // ExecutorCompletionService
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

	    compServ.submit(new ComputeNewPosition(i, map.getPosition(i), deltaTime,
		    interactionMatrix, newMap), true);
	    // log("submitted task " + i + " " + j);
	}

	try {
	    for (int n = 0; n < numBodies; n++) {

		compServ.take();
		if (state.isStopped()) {
		    // Per lo stop viene usata la classe
		    // ExecutorCompletionService
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

	view.setUpdated(newMap);
    }// doCompute()

    private void doReset() {
	var.setNumBodies(0);
	map = new BodiesMap(0);
	interactionMatrix = new InteractionMatrix(0);
    }

    private void doRandomize() throws InterruptedException {
	int numBodies = var.getNumBodies();
	this.map = new BodiesMap(numBodies);
	interactionMatrix = new InteractionMatrix(numBodies);
	Bodies.getInstance().makeRandomBodies(numBodies);

	map.generateRandomMap();
	view.setUpdated(map);

	// log("\n" + map.toString());
	// log("\n" + Bodies.getInstance().toString());

    }// doRandomize()

    public void run() {
	initPool();
	while (true) {
	    try {
		while (true) {
		    state.waitAction();
		    if (!state.isStopped()) {
			doCompute();
		    } else
			doRandomize();
		    if (state.isStopped()) {
			log("Stopped");
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
	synchronized (System.out) {
	    System.out.println("[MASTER] : " + error);
	}
    }
}
