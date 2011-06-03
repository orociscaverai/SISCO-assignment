package nbody_distribuito.master;

import java.io.Serializable;

public class ClientResponse implements Serializable{

	private static final long serialVersionUID = 1L;
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
