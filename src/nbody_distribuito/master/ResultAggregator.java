package nbody_distribuito.master;

import java.util.List;

import nbody_distribuito.BodiesMap;
import nbody_distribuito.Body;

public class ResultAggregator {

    private static final int dimension = 2;
    private float[][] acceleration;
    private float[][] position;
    private float[][] velocity;
    private int numBodies;
    private float deltaTime;
    private float deltaTimeSqr;

    public ResultAggregator(int numBodies, float deltaTime) {
	this.numBodies = numBodies;
	this.acceleration = new float[numBodies][dimension];
	this.velocity = new float[numBodies][dimension];
	this.position = new float[numBodies][dimension];
	this.deltaTime = deltaTime;
	this.deltaTimeSqr = deltaTime * deltaTime;
    }

    /**
     * Questo metodo inizia a precalcolare parte dell'equazione del moto che
     * porterà al risultato finale con i dati dello step di computazione
     * precedente
     * */
    public void initialize(BodiesMap map) {

	// Per tutti i corpi preleva dalla mappa la velocità e la posizione
	// dello step precedente
	for (int i = 0; i < numBodies; i++) {
	    Body b = map.getBody(i);
	    float[] oldVel = b.getVelocity();
	    float[] oldPos = b.getPosition();
	    for (int j = 0; j < dimension; j++) {
		position[i][j] = oldVel[j] * deltaTime + oldPos[j];
		velocity[i][j] = oldVel[j]; // v0
		acceleration[i][j] = 0;
	    }
	}
    }

    /**
     * Ricevute le accelerazioni parziali calcolate dai worker, il metodo esegue
     * l'ultima fase della computazione. Per fare ciò utilizza delle variabili in stile accumulatore.
     * 
     * 
     * 
     * 
     * */
    public void aggregate(JobResult res) {
	List<ClientResponse> resultList = res.getResultList();
	for (int i = 0; i < resultList.size(); i++) {
	    ClientResponse cr = resultList.get(i);
	    int id = cr.getBodyId();
	    float[] partialAcceleration = cr.getPartialAccelation();

	    for (int j = 0; j < dimension; j++) {
		// a = SUM(ai);
		acceleration[id][j] += partialAcceleration[j];
		
		// v =  a * t
		velocity[id][j] += partialAcceleration[j] * deltaTime;
		position[id][j] += 0.5 * partialAcceleration[j] * deltaTimeSqr;
	    }
	}
    }
}
