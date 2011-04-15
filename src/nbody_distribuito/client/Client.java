package nbody_distribuito.client;

import java.io.IOException;
import java.net.UnknownHostException;

import nbody_distribuito.Constants;

import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;
import pcd.actors.Resource;
import pcd.actors.filters.MsgFilter;

public class Client extends Actor {

    private static final int ACQUIRE = 0;

    private String serverAddress;
    private String serverName;
    private MsgFilter filter;

    protected Client(String actorName, String serverName, String serverAddress,
	    MsgFilter filter) {
	super(actorName);
	this.serverName = serverName;
	this.serverAddress = serverAddress;
	this.filter = filter;
    }

    private void connect() {
	Port port = new Port(serverName, serverAddress);
	// invio la richiesta per associarmi al Master
	try {
	    Message m = new Message("request", Constants.ASSOCIATE,
		    this.getLocalPort());
	    send(port, m);
	    log(" - invio messaggio: request(ASSOCIATE, " + port + ")");
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

    @Override
    public void run() {
	connect();

    }

    private void log(String msg) {
	System.out.println(getActorName() + msg);
    }
}