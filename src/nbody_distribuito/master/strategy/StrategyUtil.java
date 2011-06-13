package nbody_distribuito.master.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nbody_distribuito.model.BodiesMap;
import nbody_distribuito.shared_object.Job;

/**
 * Classe astratta che implementa metodi utili per la definizione
 *  di una strategia
 * @author Boccacci Andrea, Cicora Saverio
 *
 */
public abstract class StrategyUtil implements PartitionStrategy{

	private Map<Integer, Integer> relativeIndexMap;

	private List<Job> listOfJob =  new ArrayList<Job>();
	private int nextJobToGet = 0;

	@Override
	public abstract void splitJob(int numOfWorker, BodiesMap map);

	/**
	 * Metodo che restituisce il Job successivo
	 */
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
	
	/**
	 * Metodo che aggiunge l'interazione tra due corpi nel job Corrente
	 * @param indexA indice assoluto del primo corpo
	 * @param indexB indice assoluto del secondo corpo
	 */
	protected void addInteractionToCurrentJob(int indexA, int indexB){
		//Prendo gli indici relativi a partire dagli assoluti
		int relativeA = relativeIndexMap.get(indexA);
		int relativeB = relativeIndexMap.get(indexB);
		//Job corrente
		Job curr = listOfJob.get(listOfJob.size()-1);
		//aggiungo L'interazione al Job
		curr.addInteraction(relativeA, relativeB);
	}
	/**
	 * Metodo che aggiunge un corpo al job corrente, 
	 * se il corpo è già presente non succede nulla
	 * @param id identificativo assoluto del corpo
	 * @param pos posizione del corpo
	 * @param mass massa del corpo
	 */
	protected void addDataToCurrentJob(int id, float[] pos, float mass){
		if(relativeIndexMap.containsKey(id)){
			//il job ha già al suo interno il corpo..
			return;
		}
		//Job corrente
		Job curr = listOfJob.get(listOfJob.size()-1);
		//aggiungo all'hash degli indici l'id del nuovo corpo e gli assogio la nuova posizione
		relativeIndexMap.put(id, curr.getNumBodies());
		//aggiungo il corpo
		curr.addData(id, pos, mass);
	}
	/**
	 * Metodo che crea un nuovo Job
	 */
	protected void createNewJob(){
		relativeIndexMap = new HashMap<Integer, Integer>();
		listOfJob.add(new Job());
	}
	/**
	 * Metodo che inizializza la JobList (il numero di Job è una indicazione)
	 * @param numJobs
	 */
	protected void initializeJobList(int numJobs){
		listOfJob =  new ArrayList<Job>(numJobs);
	}

	/**
	 * Metodo che restituisce il numero di Lavori Generati
	 * @return
	 */
	protected int getNumOfJobs(){
		return listOfJob.size();
	}

}
