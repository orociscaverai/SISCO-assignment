package nbody;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nbody.exceptions.StoppedException;

public class Master extends Thread {
	private ExecutorService executor;
	private InteractionMatrix interactionMatrix;
	private BodiesMap map;
	private int poolSize;
	private float deltaTime;
	private float softFactor;
	private StateMonitor state;
	private StateVariables var;
	private int numBodies;

	public Master(StateMonitor state, StateVariables var) {
		super("Master");

		this.poolSize = Runtime.getRuntime().availableProcessors() * 3;
		this.var = var;
		this.state = state;

	}

	private void doCompute() throws StoppedException,InterruptedException {

		executor = Executors.newFixedThreadPool(poolSize);
		deltaTime = var.getDeltaTime();
		softFactor = var.getSoftFactor();

		// Combinazioni senza ripetizioni di tutti i Bodies
		// int numTask = (numBodies * (numBodies - 1) * (numBodies - 2)) / 2;
		for (int i = 0; i < numBodies; i++) {
			for (int j = i + 1; j < numBodies; j++) {
				executor.execute(new ComputeMutualAcceleration(i, j,
						interactionMatrix, map, softFactor));
				// log("submitted task " + i + " " + j);
			}
		}

		BodiesMap newMap = new BodiesMap(numBodies);
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			log("Catch Exception 1... shutting down now");
			executor.shutdownNow();
			log("shutdown passed");
			throw new StoppedException();
		}
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
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			log("Catch Exception ... shutting down now");
			executor.shutdownNow();
			log("shutdown passed");
			throw new StoppedException();
		}
		var.putMap(newMap);
		this.map = newMap;
	} // doCompute()


//TODO unused
/*	private void doReset() throws InterruptedException{
		//this.numBodies = 0;
		//TODO
		var.clearPendingMaps();
		doRandomize();
	}
*/
	private void doRandomize() throws InterruptedException {
		this.map = new BodiesMap(numBodies);
		Bodies.getInstance().makeRandomBodies(numBodies);

		map.generateRandomMap();
		System.out.println("messo");
		var.putMap(map);

		// log("\n" + map.toString());
		// log("\n" + Bodies.getInstance().toString());

	}// doRandomize()

	public void run() {
		while(true){
			try {
				state.waitRandomize();
				numBodies = var.getNumBodies();
				interactionMatrix = new InteractionMatrix(numBodies);
				doRandomize();
				while (true) {
					state.waitStart();
					doCompute();
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				log("restarting");
			}
		}
	}
	private void log(String error) {
		System.out.println("[MASTER] : "+error);

	}
}
