package nbody_distribuito.master;

import java.util.List;

public class JobResult {
	private List<ClientResponse> response;
	
	public List<ClientResponse> getResultList(){
		return response;
	}
}
