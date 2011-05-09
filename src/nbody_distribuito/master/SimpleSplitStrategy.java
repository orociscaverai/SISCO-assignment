package nbody_distribuito.master;

import nbody_distribuito.BodiesMap;

/**
 * @author Boccacci Andrea, Cicora Saverio
 * 
 *         Questa classe implementa una semplice strategia per la suddivisione
 *         della mappa: si limita a creare n mappe con m corpi senza prendere in
 *         considerazione altro.
 * 
 */
public class SimpleSplitStrategy implements PartitionStrategy {

    @Override
    public Tree partitionMap(int numOfWorker, BodiesMap map) {
	
	return null;
    }

}
