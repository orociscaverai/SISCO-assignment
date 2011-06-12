package nbody.model;


/**
 * Effetua i calcoli della prima fase della computazione: il calcolo delle
 * interazioni. Un'istanza di tale classe viene creata dal master ma il metodo
 * run() viene eseguito dal worker.
 */
public class ComputeMutualAcceleration implements Runnable {
    private int a, b;
    private final static int dimension = 2;
    private InteractionMatrix interactionMatrix;
    private BodiesMap map;
    private float softFactor;

    /**
     * Nel costruttore vengono passati i dati necessari per eseguire il Task:
     * 
     * @param indexA
     *            , indexB : rappresentano gli indici dei due corpi da
     *            computare, essi permetteranno di recuperare i dati dei corpi
     *            dalla struttura dati Bodies
     * @param interactionMatrix
     *            struttura dati nella quale verranno salvate le accelerazioni
     *            parziali calcolate
     * @param map
     *            struttura dati dalla quale verranno presi i dati relativi alla
     *            posizione di un corpo per calcolare le interazioni.
     * @param softFactor
     *            parametro per ridurre la magnitudine delle forze quando i
     *            corpi sono molto vicini. Evita la divisione per zero.
     */
    public ComputeMutualAcceleration(int indexA, int indexB, InteractionMatrix interactionMatrix,
	    BodiesMap map, float softFactor) {
	this.map = map;
	this.a = indexA;
	this.b = indexB;
	this.softFactor = softFactor;
	this.interactionMatrix = interactionMatrix;
    }

    @Override
    public void run() {
	bodyBodyInteraction(a, b, softFactor);
    }

    private void bodyBodyInteraction(int indexA, int indexB, float soft2) {

	// Recupero i dati relativi alla posizione
	Body a = Bodies.getInstance().getBody(indexA);
	Body b = Bodies.getInstance().getBody(indexB);
	float[] bi = map.getPosition(indexA);
	float[] bj = map.getPosition(indexB);
	float[] rij = new float[dimension];

	// Distanza tra il Body i e il Body j
	for (int i = 0; i < dimension; i++)
	    rij[i] = bj[i] - bi[i];

	// Distanza al quadrato
	float distSqr = 0;
	for (int i = 0; i < dimension; i++)
	    distSqr += rij[i] * rij[i];
	distSqr += soft2;

	// invDistCube = G / distSqr^(3/2)
	float invDistCube = (float) (1.0 / Math.sqrt(distSqr * distSqr * distSqr));

	float[] ai = new float[dimension];
	float[] aj = new float[dimension];

	// calcolo delle accelerazioni
	for (int i = 0; i < dimension; i++) {
	    ai[i] = rij[i] * b.getMass() * invDistCube;
	    aj[i] = rij[i] * a.getMass() * invDistCube * -1;
	}

	// Salvo i risultati
	interactionMatrix.setAcceleration(indexA, indexB, ai);
	interactionMatrix.setAcceleration(indexB, indexA, aj);
    }

}
