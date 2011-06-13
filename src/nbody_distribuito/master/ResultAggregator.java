package nbody_distribuito.master;

import java.util.List;

import nbody_distribuito.model.BodiesMap;
import nbody_distribuito.model.Body;
import nbody_distribuito.shared_object.ClientResponse;

/**
 * Classe che si occupa di inizializzare una mappa e aggiungere i risultati
 * parziali per arrivare ad una mappa completa
 * @author Boccacci Andrea, Cicora Saverio
 *
 */
public class ResultAggregator {

    private BodiesMap bm;

    private static final int dimension = 2;
    // private float[][] acceleration;

    private int numBodies;
    private float deltaTime;

    /**
     * 
     * @param numBodies
     *            : numero totale di corpi dell'intera simulazione
     */
    public ResultAggregator(int numBodies, float deltaTime) {
	this.numBodies = numBodies;
	this.bm = new BodiesMap(numBodies);
	// this.acceleration = new float[numBodies][dimension];

	this.deltaTime = deltaTime;
    }

    /**
     * Questo metodo inizia a precalcolare parte dell'equazione del moto che
     * porterà al risultato finale con i dati dello step di computazione
     * precedente
     * */
    public void initialize(BodiesMap oldMap) {

	// Per tutti i corpi preleva dalla mappa la velocità e la posizione
	// dello step precedente
	for (int i = 0; i < numBodies; i++) {
	    Body b = oldMap.getBody(i);
	    float[] oldVel = b.getVelocity();
	    float[] oldPos = b.getPosition();

	    float[] position = new float[dimension];
	    float[] velocity = new float[dimension];
	    for (int j = 0; j < dimension; j++) {

		position[j] = oldVel[j] * deltaTime + oldPos[j];
		velocity[j] = oldVel[j]; // v0
		// acceleration[i][j] = 0;
	    }
	    //creo un nuovo corpo e lo aggiungo alla mappa
	    Body newBody = new Body(i,b.getMass(),position);
	    newBody.setVelocity(velocity);
	    bm.addBody(newBody);
	}
    }

    /**
     * Ricevute le accelerazioni parziali calcolate dai worker, il metodo esegue
     * l'ultima fase della computazione. Per fare ciò utilizza delle variabili
     * in stile accumulatore.
     * */
    public void aggregate(List<ClientResponse> resultList) {
	for (int i = 0; i < resultList.size(); i++) {
		//per ogni corpo contenuto nel risultato prendo posizione e velocità
	    ClientResponse cr = resultList.get(i);
	    int id = cr.getBodyId();

	    Body b = bm.getBody(id);

	    float[] position = b.getPosition();
	    float[] velocity = b.getVelocity();

	    float[] partialdisplacement = cr.getPartialDisplacement();
	    float[] partialVelocity = cr.getPartialVelocity();
	    for (int j = 0; j < dimension; j++) {
		// a = SUM(ai);
		// acceleration[id][j] += partialAcceleration[j];

		// v = a * t
		// aggiungo la parte colcolta ai miei dati
		velocity[j] += partialVelocity[j];
		position[j] += partialdisplacement[j];
	    }
	    //aggiorno il corpo nella mappa
	    b.setVelocity(velocity);
	    b.setPosition(position);
	}
    }
/**
 * Questo metodo va chiamato solo quando tutti i lavori sono terminati 
 * restituisce la mappa risultato della computazione
 * @return risultato della computazione
 */
    public BodiesMap getResultMap() {
	return bm;
    }
}
