package nbody_distribuito.master.strategy;

import nbody_distribuito.model.BodiesMap;

/**
 * Strategia di base che mette 40 interazioni in ogni Job
 * @author Boccacci Andrea, Cicora Saverio
 *
 */
public class FixedJobStrategy extends StrategyUtil {
	private static final int MAX_INTERACTION_PER_JOB = 40;

	@Override
	public void splitJob(int numOfWorker, BodiesMap map) {
		int numBodies = map.getNumBodies();
		int interactions = numBodies*(numBodies-1)/2;
		int numOfJob = interactions/MAX_INTERACTION_PER_JOB +1;
		initializeJobList(numOfJob);
		int interactionsLoaded = 0;
		createNewJob();
		for(int i = 0 ; i< numBodies-1; i++){
			addDataToCurrentJob(i, map.getBody(i).getPosition(), map.getBody(i).getMass());
			for (int j = i+1; j<numBodies; j++){
				if (interactionsLoaded == MAX_INTERACTION_PER_JOB){
					interactionsLoaded = 0;
					createNewJob();
					addDataToCurrentJob(i, map.getBody(i).getPosition(), map.getBody(i).getMass());
				}
				addDataToCurrentJob(j, map.getBody(j).getPosition(), map.getBody(j).getMass());
				try {
					addInteractionToCurrentJob(i, j);
					interactionsLoaded++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//e.printStackTrace();
				}
			}
		}
		
	}

}
