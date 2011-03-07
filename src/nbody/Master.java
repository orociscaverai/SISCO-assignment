package nbody;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
		executor = Executors.newFixedThreadPool(poolSize);
		this.var = var;
		this.state = state;

	}

	private void doCompute() throws InterruptedException {

		deltaTime = var.getDeltaTime();
		softFactor = var.getSoftFactor();

		// Combinazioni senza ripetizioni di tutti i Bodies
		int numTask = (numBodies * (numBodies - 1) ) / 2;
		CountDownLatch count = new CountDownLatch(numTask);
		//BoundedCounter count = new BoundedCounter(numTask);
		for (int i = 0; i < numBodies-1; i++) {
			for (int j = i + 1; j < numBodies; j++) {
				executor.execute(new ComputeMutualAcceleration(i, j,
						interactionMatrix, map, softFactor, count));
				// log("submitted task " + i + " " + j);
			}
		}

		BodiesMap newMap = new BodiesMap(numBodies);
		count.await();
		
		numTask = numBodies;
		count = new CountDownLatch(numTask);
		for (int i = 0; i < numBodies; i++) {
			executor.execute(new ComputeNewPosition(i, map.getPosition(i),
					deltaTime, interactionMatrix, newMap, count));
			// log("submitted task " + i + " " + j);
		}
		
		count.await();
		
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
				boolean stopped = true;
				while (true) {
					if(stopped){
						state.waitStart();
						stopped = false;
					}
					doCompute();
					if(state.isStopped()){
						break;
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				log("restartingTime "+System.currentTimeMillis());
			}
		}
	}
	private void log(String error) {
		System.out.println("[MASTER] : "+error);

	}
}
