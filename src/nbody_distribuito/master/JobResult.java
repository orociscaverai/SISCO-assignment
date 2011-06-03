package nbody_distribuito.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JobResult implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<ClientResponse> response;
	
	public JobResult(){
		response = new ArrayList<ClientResponse>();
	}

    public List<ClientResponse> getResultList() {
	return response;
    }

    public void addResult(ClientResponse cr) {
	response.add(cr);
    }

    public void addResult(int id, float[] partialAcceleration) {
	response.add(new ClientResponse(id, partialAcceleration));
    }
}
