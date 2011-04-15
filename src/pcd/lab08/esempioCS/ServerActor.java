package pcd.lab08.esempioCS;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;
import pcd.actors.Resource;

public class ServerActor extends Actor {
    private static final int ASSOCIATE = 0;

    private List<Port> pending = null;

    public ServerActor(String actorName) {
	super(actorName);
	pending = new ArrayList<Port>();
    }

    public void run() {
	while (true) {
	    log(" - in attesa di richieste ...");

	    Message message = receive();
	    log(" - messaggio ricevuto: " + message);
	    int kind = (Integer) message.getArg(0);
	    Port clientPort = (Port) message.getArg(1);

	    if (kind == ASSOCIATE) {
		try {
		    pending.add(clientPort);
		    send(clientPort, new Message("reply", new Resource(
			    "ASSOCIATED")));
		} catch (UnknownHostException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}

	    }
	}
    }

    private void log(String msg) {
	System.out.println(getActorName() + msg);
    }
}