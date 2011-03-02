package nbody;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Master extends Thread {
	private ExecutorService executor;
	private InteractionMatrix interactionMatrix;
	private BodiesMap map;
	//private NBodyView view;
	private int numBodies;
	private int poolSize;
	private float deltaTime;
	private float softFactor;
	private ArrayBlockingQueue<BodiesMap> mapQueue;

	public Master(int numBodies,ArrayBlockingQueue<BodiesMap> mapQueue) {
		super("Master");

		this.poolSize = Runtime.getRuntime().availableProcessors() * 3;
		this.deltaTime = Float.parseFloat(System
				.getProperty("deltaTime", "0.5"));
		this.numBodies = 0;
		this.softFactor = 1f;
		this.mapQueue = mapQueue;
		this.numBodies = numBodies;

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

		BodiesMap newMap = new BodiesMap(numBodies);
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
		mapQueue.put(newMap);
		this.map = newMap;
	} // doCompute()

	private void doReset() throws InterruptedException{
		this.numBodies = 0;
		mapQueue.clear();
		doRandomize();
	}

	private void doRandomize() throws InterruptedException {
		this.map = new BodiesMap(numBodies);
		Bodies.getInstance().makeRandomBodies(numBodies);

		map.generateRandomMap();
		System.out.println("messo");
		mapQueue.put(map);

		// log("\n" + map.toString());
		// log("\n" + Bodies.getInstance().toString());

	}// doRandomize()

	public void run() {
		interactionMatrix = new InteractionMatrix(numBodies);
		try {
			doRandomize();
			while (true) {
				doCompute();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
