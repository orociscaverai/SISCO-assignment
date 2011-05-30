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
	
	public ResultAggregator(int numBodies, float deltaTime){
		this.numBodies = numBodies;
		this.acceleration = new float[numBodies][dimension];
		this.velocity = new float[numBodies][dimension];
		this.position = new float[numBodies][dimension];
		this.deltaTime = deltaTime;
	}
	public void initialize(BodiesMap map){
		for (int i = 0; i< numBodies; i++){
			Body b = map.getBody(i);
			float[] oldVel = b.getVelocity();
			float[] oldPos = b.getPosition();
			for (int j = 0; j<dimension;j++){
				position[i][j] = oldVel[j]* deltaTime + oldPos[j];
				velocity[i][j] = oldVel[j];
				acceleration[i][j] = 0;
			}
		}
	}
	public void aggregate(JobResult res){
		List<ClientResponse> resultList = res.getResultList();
		for (int i = 0; i<resultList.size(); i++){
			ClientResponse cr = resultList.get(i);
			int id = cr.getBodyId();
			float[] partialAcceleration = cr.getPartialAccelation();

			for (int j = 0; j<dimension; j++){
				acceleration[id][j] += partialAcceleration[j];
				velocity[id][j] += partialAcceleration[j]*deltaTime;
				position[id][j] += 3/2*partialAcceleration[j]*Math.pow(deltaTime,2);
			}
		}
	}
}
