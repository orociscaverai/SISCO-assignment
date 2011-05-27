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

    List<Pippo> listOfJob;

    @Override
    public void splitJob(int numOfWorker, BodiesMap map) {
	int numOfJob = (int) map.getNumBodies() / numOfWorker;

	int numOfJobR = map.getNumBodies() % numOfWorker;

	List<Pippo> pippoList = new ArrayList<Pippo>(numOfWorker);
	int indexOfBodies = 0;
	for (int i = 0; i < numOfWorker; i++) {

	    int n = numOfJob;
	    if (numOfJobR > 0) {
		n += 1;
		numOfJobR--;
	    }

	    Pippo ii = new Pippo();
	    ii.firstIndex = indexOfBodies;
	    indexOfBodies += n;
	    ii.lastIndex = indexOfBodies - 1;
	    pippoList.add(ii);
	}

	for (Pippo p : pippoList) {
	    System.out.println(p.toString());
	}
for (int ii = 0 ; ii < ){}	
	
	
    }
    List<List<ClientData>> listOfJob;

    @Override
    public List<ClientData> getNextJob() {

	
	
	return null;
    }

}

class Pippo {

    public int firstIndex;
    public int lastIndex;

    public String toString() {
	return "" + firstIndex + "----" + lastIndex + "\n";
    }
}