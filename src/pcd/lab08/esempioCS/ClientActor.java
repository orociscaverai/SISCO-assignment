package pcd.lab08.esempioCS;

import java.io.IOException;
import java.net.UnknownHostException;

import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;
import pcd.actors.Resource;
import pcd.actors.filters.MsgFilter;

public class ClientActor extends Actor {
    private static final int ACQUIRE = 0;
    private String serverName;
    private MsgFilter filter;

    protected ClientActor(String actorName, String serverName, MsgFilter filter) {
	super(actorName);
	this.serverName = serverName;
	this.filter = filter;
    }

    public void run() {
	Port port = new Port(serverName, "localhost");

	// invio la richiesta per acquisire una risorsa
	try {
	    send(port, new Message("request", ACQUIRE, this.getLocalPort()));
	    log(" - invio messaggio: request(ACQUIRE, " + port + ")");
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	// ricevo il messaggio di risposta
	Message msg = null;

	if (filter == null)
	    msg = receive();
	else
	    msg = receive(filter);

	Resource r = (Resource) msg.getArg(0);
	log(" - ricezione messaggio");
	log(" - risorsa " + r + " allocata");

    }

    private void log(String msg) {
	System.out.println(getActorName() + msg);
    }
}