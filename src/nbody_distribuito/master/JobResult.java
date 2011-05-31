package nbody_distribuito.master;

import java.util.List;

public class JobResult {
    private List<ClientResponse> response;

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
