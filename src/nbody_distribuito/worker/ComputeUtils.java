package nbody_distribuito.worker;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nbody_distribuito.BodiesMap;

public class ComputeUtils {
    private ExecutorService ex;

    public ComputeUtils() {
	initPool();
    }

    private void initPool() {
	int poolSize = Runtime.getRuntime().availableProcessors() * 3;
	ex = Executors.newFixedThreadPool(poolSize);
    }

    private void shutdownAndReset() throws InterruptedException {
	ex.shutdownNow();
	ex.awaitTermination(2, TimeUnit.MINUTES);
	initPool();
    }

    public BodiesMap doCompute(BodiesMap bm, float deltaTime, float softFactor)
	    throws Exception {

	int numBodies = bm.getNumBodies();
	InteractionMatrix interactionMatrix = new InteractionMatrix(numBodies);
	CountDownLatch cntr;

	// Combinazioni senza ripetizioni di tutti i Bodies
	int numTask = (numBodies * (numBodies - 1)) / 2;

	// Inizio la fase 1 -----------------------------------------
	cntr = new CountDownLatch(numTask);
	try {
	    for (int i = 0; i < numBodies - 1; i++) {
		for (int j = i + 1; j < numBodies; j++) {
		    ex.submit(new ComputeMutualAcceleration(i, j,
			    interactionMatrix, bm, softFactor, cntr));
		    // log("submitted task " + i + " " + j);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

	// La creo qui per guadagnare tempo
	BodiesMap newMap = new BodiesMap(numBodies);
	try {
	    cntr.await(1L, TimeUnit.HOURS);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    // Mi assicuro di tornare ad uno stato consistente
	    shutdownAndReset();
	    throw new Exception("Gestita l'interrupt exception");
	}

	// TODO inserire il controllo sul Monitor attivo dello stop

	// Inizio la fase 2 -----------------------------------------
	cntr = new CountDownLatch(numBodies);
	for (int i = 0; i < numBodies; i++) {

	    ex.submit(new ComputeNewPosition(i, bm.getPosition(i), deltaTime,
		    interactionMatrix, newMap, cntr));
	    // log("submitted task " + i + " " + j);
	}

	try {
	    cntr.await(1L, TimeUnit.HOURS);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    // Mi assicuro di tornare ad uno stato consistente
	    shutdownAndReset();
	    throw new Exception("Gestita l'interrupt exception");
	}
	return newMap;

    }// doCompute()

    private void log(String error) {
	System.out.println("[MASTER] : " + error);

    }
}
