package nbody_distribuito.master;

public class ClientResponse {

	private int id;
	private float[] partialAcceleration;

	public ClientResponse(int id, float[] partialAcceleration){
		this.id = id;
		this.partialAcceleration = partialAcceleration;
	}
	public int getBodyId(){
		return id;
	}
	public float[] getPartialAccelation(){
		return partialAcceleration;
	}

}
