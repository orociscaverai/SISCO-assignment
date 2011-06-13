package nbody.controller;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nbody.model.Bodies;
import nbody.model.BodiesMap;
import nbody.model.InteractionMatrix;
import nbody.model.StateMonitor;
import nbody.model.StateVariables;
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
     * l'arresto della computazione e di riportare l'applicazione in uno stato
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
	// Creo i task da fare e li invio ai Worker
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

	// Mediante il medoto take() del CompletitionServices mi blocco sino
	// all'arrivo di un nuovo risultato. In questo caso non viene generato
	// un risultato, ma lo utilizzao per implementare lo stop: ad ogni
	// risultato controllo se è stato inviato un evento di stop, in questo
	// caso interrompo la computazione senza aggiornare il modello
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
	// Del tutto simile allfa fase 1 come implementazione
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
	// Tutti i calcoli sono stati fatti correttamente: aggiorno la vista col
	// nuovo modello.
	// Il nuovo modello posso usarlo come modello crrente anche se l'ho
	// inviata alla vista, dato che d'ora
	// in poi verrà acceduto solo in lettura
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

    }// doRandomize()

    public void run() {
	initPool();
	while (true) {
	    try {
		int action = state.waitAction();
		if (action == 0 || action == 2) {
		    doCompute();
		} else if (action == 1) {
		    doRandomize();
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
