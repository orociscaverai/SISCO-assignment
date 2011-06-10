package nbody_distribuito.message;

import nbody_distribuito.Constants;
import pcd.actors.Message;

public class StopComputationMessage extends Message {

    private static final long serialVersionUID = 1L;

    private static final String messageType = Constants.STOP_EVENT;

    public StopComputationMessage() {
	super(messageType);
    }

    @Override
    public String toString() {

	String args = getType();

	return args;

    }

}
