package nbody;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Master extends Thread {
    private int numBodies;
    private int deltaTime;
    private ExecutorService executor;
    private InteractionMatrix interactionMatrix;
    private Planets planets;
    private int poolSize;

    public Master(int numBodies) {
	this.poolSize = Integer.parseInt(System.getProperty("poolSize", "4"));
	this.deltaTime = Integer.parseInt(System.getProperty("deltaTime", "1"));
	this.numBodies = numBodies;
	interactionMatrix = new InteractionMatrix(numBodies);
	planets = new Planets(numBodies);
    }

    private void compute() throws InterruptedException {
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
			    interactionMatrix));
		    // log("submitted task " + i + " " + j);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}

	executor.shutdown();
	executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
	// / XXX Stampa per Debug
	System.out.println(System.currentTimeMillis() - time);
	// System.out.print(interactionMatrix.toString());
	executor = Executors.newFixedThreadPool(poolSize);
	System.out.println(System.currentTimeMillis() - time);

	for (int i = 0; i < numBodies - 1; i++) {

	    try {
		executor.execute(new ComputeNewPosition(i, deltaTime,
			interactionMatrix));
		// log("submitted task " + i + " " + j);
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	}

	executor.shutdown();
	executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
	System.out.println(System.currentTimeMillis() - time);

    } // compute()

    public void run() {
	// TODO
	// while (true){
	for (int i = 0; i < 1; i++) {
	    try {
		compute();
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

    private void log(String msg) {
	System.out.println("[SERVICE] " + msg);
    }
}