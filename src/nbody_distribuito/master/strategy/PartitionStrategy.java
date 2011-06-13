package nbody_distribuito.master.strategy;


import nbody_distribuito.model.BodiesMap;
import nbody_distribuito.shared_object.Job;

/**
 * Interfaccia che rappresenta una strategia di suddivisione del lavoro
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
