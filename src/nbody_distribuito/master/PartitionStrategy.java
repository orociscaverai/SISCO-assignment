package nbody_distribuito.master;


import nbody_distribuito.BodiesMap;

/**
 * @author Boccacci Andrea, Cicora Saverio
 * 
 */
public interface PartitionStrategy {

    public void splitJob(int numOfWorker, BodiesMap map);

    /**
     * Restituisce null in caso non ci sono task, FIXME controllare se ci sono
     * meytodi migliori
     */
    public Job getNextJob();
}
