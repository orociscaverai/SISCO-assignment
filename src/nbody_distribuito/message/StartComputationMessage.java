package nbody_distribuito.message;

import nbody_distribuito.Constants;
import pcd.actors.Message;

public class StartComputationMessage extends Message {

    private static final long serialVersionUID = 1L;

    private static final String messageType = Constants.START_EVENT;

    public StartComputationMessage() {
	super(messageType);
    }

    @Override
    public String toString() {

	String args = getType();

	return args;

    }

}
