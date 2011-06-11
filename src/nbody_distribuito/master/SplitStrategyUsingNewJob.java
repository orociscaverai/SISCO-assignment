package nbody_distribuito.master;

import java.util.ArrayList;
import java.util.List;

import nbody_distribuito.BodiesMap;

public class SplitStrategyUsingNewJob extends StrategyUtil implements PartitionStrategy {
    

    public void splitJob(int numOfWorker, BodiesMap map) {

	if (numOfWorker == 0) {
	    throw new NumWorkerException(
		    "Non posso iniziare il lavoro: numero di worker uguale a zero");
	}

	int numOfJob = (int) map.getNumBodies() / numOfWorker;
	int numOfJobR = map.getNumBodies() % numOfWorker;

	List<Integer> indexList = new ArrayList<Integer>();
	int indexOfBodies = 0;
	indexList.add(indexOfBodies);
	
	for (int i = 0; i < numOfWorker; i++) {

	    int n = numOfJob;
	    if (numOfJobR > 0) {
		n += 1;
		numOfJobR--;
	    }
	    indexOfBodies += n;
	    indexList.add(indexOfBodies);
	}

//	for (int i = 0; i < indexList.size(); i++) {
//	    System.out.println("" + indexList.get(i));
//	}
	int numOfSplit = indexList.size() - 1;
	// generazione job
	int totalJob = numOfSplit * (numOfSplit - 1);

	initializeJobList(totalJob);


	for (int i = 0; i < numOfSplit; i++) {
	    
	    int firstIndex1 = indexList.get(i);
	    int lastIndex1 = indexList.get(i + 1);
	    int firstIndex2 = indexList.get(i);
	    int lastIndex2 = indexList.get(i + 1);
	    int index = i;

	    while (true) {
		createNewJob();
		
//		log("Create job: " + firstIndex1 + " " + lastIndex1 + " "
//			+ firstIndex2 + " " + lastIndex2);
		for (int j = firstIndex1; j < lastIndex1; j++) {
		    addDataToCurrentJob(j, map.getBody(j).getPosition(), map.getBody(j).getMass());
		}
		if (firstIndex1 != firstIndex2) {
		    for (int j = firstIndex2; j < lastIndex2; j++) {
		    	addDataToCurrentJob(j, map.getBody(j).getPosition(), map.getBody(j).getMass());

		    }
		}
		// interazioni
		if (firstIndex1 != firstIndex2) {
		    for (int j = firstIndex1; j < lastIndex1; j++) {
			for (int k = firstIndex2; k < lastIndex2; k++) {
			    try {
			    	addInteractionToCurrentJob(j, k);

			    } catch (Exception e) {
				e.printStackTrace();
			    }
			}
		    }
		} else {
		    for (int j = firstIndex1; j < lastIndex1 - 1; j++) {
			for (int k = j + 1; k < lastIndex1; k++) {
			    try {
			    	addInteractionToCurrentJob(j, k);

			    } catch (Exception e) {
				e.printStackTrace();
			    }
			}
		    }
		}

		if (lastIndex2 == map.getNumBodies())
		    break;
		index += 1;
		firstIndex2 = indexList.get(index);
		lastIndex2 = indexList.get(index + 1);
	    }

	}
    }


}
