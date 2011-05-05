package nbody_distribuito;

import java.io.IOException;
import java.net.UnknownHostException;


import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;

public class FlagActor extends Actor {

    private boolean isSet = false;

    public FlagActor(String actorName) {
	super(actorName);
    }

    private boolean isSet() {
	return isSet;
    }

    private void setFlag() {
	isSet = true;
    }

    private void resetFlag() {
	isSet = false;
    }

    @Override
    public void run() {
	while (true) {
	    log("in attesa di richieste ...");

	    Message message = receive();
	    log("messaggio ricevuto: " + message);
	    
	    String type = (String) message.getType();
	    Port portActor = (Port) message.getArg(1);

	    try {
		if (type.equals(Constants.IS_SET)) {
		    Boolean b = new Boolean(isSet());
		    send(portActor, new Message(Constants.IS_SET_RESULT, b));
		} else if (type.equals(Constants.SET_FLAG)) {
		    setFlag();
		    send(portActor, new Message(Constants.SET_FLAG));
		} else if (type.equals(Constants.RESET_FLAG)) {
		    resetFlag();
		    send(portActor, new Message(Constants.RESET_FLAG));
		}
	    } catch (UnknownHostException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	}
    }

    private void log(String msg) {
	System.out.println(getActorName() + ": " + msg);
    }

}
