package nbody_distribuito;

import java.io.Serializable;

public class Message implements Serializable {

    /**
     * 
     */

    public final static int GENERIC = 0;
    public final static int CONNECT = 1;

    private int messageType;
    private static final long serialVersionUID = 7078319160337423330L;

    public Message() {
	this(GENERIC);
    }

    public Message(int messageType) {
	this.messageType = messageType;
    }

    public int getMessageType() {
	return messageType;
    }
    

}
