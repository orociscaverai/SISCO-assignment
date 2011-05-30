package nbody_distribuito.master;

import java.util.ArrayList;
import java.util.List;

import nbody_distribuito.BodiesMap;
import nbody_distribuito.Body;

/**
 * @author Boccacci Andrea, Cicora Saverio
 * 
 *         Questa classe implementa una semplice strategia per la suddivisione
 *         della mappa: si limita a creare n mappe con m corpi senza prendere in
 *         considerazione altro.
 * 
 */
public class SimpleSplitStrategy implements PartitionStrategy {

	private List<Job> listOfJob;
	private int nextJobToGet=0;

	@Override
	public void splitJob(int numOfWorker, BodiesMap map) {
		int numOfJob = (int) map.getNumBodies() / numOfWorker;

		int numOfJobR = map.getNumBodies() % numOfWorker;

		//in questa lista ci sono gli indici finali di ogni set di pianeti
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

		for (int i = 0;i<indexList.size();i++ ) {
			System.out.println(""+indexList.get(i));
		}
		int numOfSplit = indexList.size()-1;
		//generazione job
		int totalJob = numOfSplit*(numOfSplit-1);
		listOfJob = new ArrayList<Job>(totalJob);

		System.out.println("num of split = "+ numOfSplit);
		for (int i = 0; i < numOfSplit; i++){
			int firstIndex1 = indexList.get(i);
			int lastIndex1 = indexList.get(i+1);
			int firstIndex2 = indexList.get(i);
			int lastIndex2 = indexList.get(i+1);
			int index = i;
			while(true){
				Job job = new Job();
				System.out.println("Create job: "+firstIndex1+ " "+lastIndex1+ " "+ firstIndex2+" "+ lastIndex2);
				for (int j = firstIndex1; j< lastIndex1; j++ ){
					job.addDataOnFirstSet(j, map.getBody(j).getPosition(), map.getBody(j).getMass());
				}
				for (int j = firstIndex2; j<lastIndex2; j++ ){
					job.addDataOnSecondSet(j, map.getBody(j).getPosition(), map.getBody(j).getMass());
				}
				listOfJob.add(job);
				if(lastIndex2 == map.getNumBodies())
					break;
				index += 1;
				firstIndex2 = indexList.get(index);
				lastIndex2 = indexList.get(index+1);
			}
		}
	}

	@Override
	public Job getNextJob() {
		//TODO gestire eccezioni
		try{
			Job result = listOfJob.get(nextJobToGet);
			nextJobToGet += 1;
			return result;
		}catch (Exception e){
			return null;
		}

	}

}
