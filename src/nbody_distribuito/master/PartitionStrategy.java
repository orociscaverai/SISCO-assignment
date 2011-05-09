package nbody_distribuito.master;

import nbody_distribuito.BodiesMap;

/**
 * @author Boccacci Andrea, Cicora Saverio
 * 
 */
public interface PartitionStrategy {

    public Tree partitionMap( int numOfWorker, BodiesMap map);
}
