package nbody_distribuito.message;

import nbody_distribuito.Constants;
import pcd.actors.Message;

public class ChangeParamMessage extends Message {

    private static final long serialVersionUID = 1L;

    private static final String messageType = Constants.CHANGE_PARAM;

    private final float deltaTime, softFactor;

    public ChangeParamMessage(float deltaTime, float softFactor) {
	super(messageType);
	this.deltaTime = deltaTime;
	this.softFactor = softFactor;
    }

    /**
     * @return the deltaTime
     */
    public float getDeltaTime() {
	return deltaTime;
    }

    /**
     * @return the softFactor
     */
    public float getSoftFactor() {
	return softFactor;
    }

    @Override
    public String toString() {

	String args = getType() + " ";

	args += "deltaTime: ";
	args += deltaTime;
	args += " softFactor: ";
	args += softFactor;

	return args;

    }

}
