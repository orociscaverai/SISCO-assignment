package nbody_distribuito.worker;

import java.io.IOException;
import java.net.UnknownHostException;

import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;
import pcd.actors.filters.MsgFilter;

public class Worker extends Actor {

    private Port masterPort;

    protected Worker(String actorName, String serverName, String serverAddress,
	    MsgFilter filter) {
	super(actorName);
	this.setLocalPort(new Port(actorName, "192.168.100.100"));
	masterPort = new Port(serverName, serverAddress);
    }

    private boolean connect() {
	Message m;
	// invio la richiesta per associarmi al Master
	try {
	    m = new Message("associate", this.getLocalPort());
	    log("Ho preparato il seguente messaggio: " + m.toString());
	    log("Lo invio a: " + masterPort.toString());
	    send(masterPort, m);

	    log(" - invio messaggio:" + m);
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	// ricevo il messaggio di risposta
	m = receive();
	if (!m.getType().equalsIgnoreCase("ACK_Associate")) {
	    log("Il master non ha apprezzato la mia associazione");
	    log("Il master ha risposto con: " + m.toString());
	    return false;
	}
	log("messaggio ricevuto, associaizione avvenuta " + m.toString());
	return true;
    }

    @Override
    public void run() {
	boolean connected = connect();
	if (connected) {
	    while (true) {
	    }

	}

	log("Termino spontaneamente l'esecuzione");
    }

    private void log(String msg) {
	System.out.println(getActorName() + ": " + msg);
    }
}