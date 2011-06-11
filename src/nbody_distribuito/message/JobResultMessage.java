package nbody_distribuito.message;

import java.util.List;

import nbody_distribuito.Constants;
import nbody_distribuito.shared_object.ClientResponse;
import pcd.actors.Message;

public class JobResultMessage extends Message {

    private static final long serialVersionUID = 1L;

    private static final String messageType = Constants.JOB_RESULT;

    private final int workerID;
    private final List<ClientResponse> result;

    public JobResultMessage(int workerID, List<ClientResponse> result) {
	super(messageType);

	this.workerID = workerID;
	this.result = result;
    }

    /**
     * @return the workerID
     */
    public int getWorkerID() {
	return workerID;
    }

    /**
     * @return the result
     */
    public List<ClientResponse> getResult() {
	return result;
    }

    @Override
    public String toString() {

	String args = getType() + " ";

	args += "workerID: ";
	args += workerID;
	args += "Result: ";
	// TODO
	args += " TODO FOr della lista";

	return args;

    }

}
