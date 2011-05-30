package nbody_distribuito.master;

public class ClientResponse {

	private int id;
	private float[] partialAcceleration;
	private float[] partialVelocity;
	private float[] partialDisplacement;
	
	public ClientResponse(int id, float[] partialAcceleration, float[] partialVelocity, float[] partialDisplacement){
		this.id = id;
		this.partialAcceleration = partialAcceleration;
		this.partialVelocity = partialVelocity;
		this.partialDisplacement = partialDisplacement;
	}
	public int getBodyId(){
		return id;
	}
	public float[] getPartialAccelation(){
		return partialAcceleration;
	}
	public float[] getPartialVelocity(){
		return partialVelocity;
	}
	public float[] getPartialDisplacement(){
		return partialDisplacement;
	}
}
