package nbody.controller;

import nbody.model.Bodies;
import nbody.model.BodiesMap;
import nbody.model.Body;
import nbody.model.InteractionMatrix;


/**
 * Effetua i calcoli della deconda fase della computazione: il calcolo della
 * nuova posizione. Un'istanza di tale classe viene creata dal master ma il
 * metodo run() viene eseguito dal worker.
 */
public class ComputeNewPosition implements Runnable {
    final private static int dimension = 2;
    private int bodyIndex;
    private float deltaTime;
    private InteractionMatrix interactionMatrix;
    private float[] oldPos;
    private BodiesMap map;

    /**
     * Nel costruttore vengono passati i dati necessari per eseguire il Task:
     * 
     * @param bodyIndex
     *            rappresenta l'indice del corpo da computare, permette di
     *            recuperare i dati del corpo dalla struttura dati Bodies
     * @param oldPos
     *            la vecchia posizione del corpo in base alla quale verrà
     *            calcolata la nuova
     * @param deltaTime
     *            tempo logico di integrazione
     * @param interactionMatrix
     *            struttura dati dalla quale verrà recuperata l'accelerazione
     *            totale che agisce sul corpo
     * @param newMap
     *            struttura dati nella quale verrà salvata la nuova posizione
     */
    public ComputeNewPosition(int bodyIndex, float[] oldPos, float deltaTime,
	    InteractionMatrix interactionMatrix, BodiesMap newMap) {
	this.interactionMatrix = interactionMatrix;
	this.bodyIndex = bodyIndex;
	this.deltaTime = deltaTime;
	this.map = newMap;
	this.oldPos = oldPos;
    }

    public void run() {
	// recuper l'accelerazione totale che agisce sul corpo
	float[] acc = interactionMatrix.getResultAcceleration(bodyIndex);

	Body old = Bodies.getInstance().getBody(bodyIndex);
	float[] vel = old.getVelocity();

	// calcolo la nuova velocità
	old.setVelocity(computeVelocity(acc, vel));

	map.setPosition(bodyIndex, computePosition(acc, vel, oldPos));

    }

    /**
     * Calcola la nuova velocità in base all'accelerazione e alla vecchia
     * velocità
     */
    private float[] computeVelocity(float[] acceleration, float[] vel) {

	float[] newVel = new float[dimension];

	for (int i = 0; i < dimension; i++) {
	    newVel[i] = ((acceleration[i] * deltaTime) + vel[i]);
	}
	return newVel;
    }

    /** Clacola la nuova posizione */
    private float[] computePosition(float[] acceleration, float[] vel, float[] oldPos) {

	float[] newPos = new float[dimension];

	// System.out.println("Init:" + newPos[0] + " " + newPos[1]);
	for (int d = 0; d < dimension; d++) {
	    newPos[d] = 0.5f * acceleration[d] * deltaTime * deltaTime + vel[d] * deltaTime
		    + oldPos[d];
	}

	return newPos;
    }

}
