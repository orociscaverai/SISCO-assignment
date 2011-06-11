package nbody_distribuito.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import nbody_distribuito.BodiesMap;

public abstract class StrategyUtil implements PartitionStrategy{

	private Map<Integer, Integer> relativeIndexMap;

	private List<Job> listOfJob =  new ArrayList<Job>();
	private int nextJobToGet = 0;

	@Override
	public abstract void splitJob(int numOfWorker, BodiesMap map);

	public Job getNextJob() {

		try {
			Job result = listOfJob.get(nextJobToGet);
			nextJobToGet += 1;
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	protected void log(String msg) {
		synchronized (System.out) {
			System.out.println("SplitStrategyUsingNewJob: " + msg);
		}
	}
	protected void addInteractionToCurrentJob(int indexA, int indexB){
		int relativeA = relativeIndexMap.get(indexA);
		int relativeB = Integer.valueOf(relativeIndexMap.get(indexB));
		Job curr = listOfJob.get(listOfJob.size()-1);
		curr.addInteraction(relativeA, relativeB);
	}
	protected void addDataToCurrentJob(int id, float[] pos, float mass){
		if(relativeIndexMap.containsKey(id)){
			//il job ha già al suo interno il corpo..
			return;
		}
		Job curr = listOfJob.get(listOfJob.size()-1);
		relativeIndexMap.put(id, curr.getNumBodies());
		curr.addData(id, pos, mass);
	}
	protected void createNewJob(){
		relativeIndexMap = new HashMap<Integer, Integer>();
		listOfJob.add(new Job());
	}
	protected void initializeJobList(int numJobs){
		listOfJob =  new ArrayList<Job>(numJobs);
	}

	protected int getNumOfJobs(){
		return listOfJob.size();
	}

}
