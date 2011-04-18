package nbody_distribuito.master;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;

public class Master extends Actor {

    private Vector<Port> workers;
    private Port computeAct = new Port("Compute", "localhost");

    public Master(String actorName) {
	super(actorName);
	workers = new Vector<Port>();
    }

    // private void compute() {
    // PartitionStrategy ps = new MyStrategy();
    // Tree tr = ps.partitionMap();
    // }

    @Override
    public void run() {
	while (true) {
	    try {
		log("wait");
		Message res = receive();
		if (res.getType().equalsIgnoreCase("associate")) {
		    log("" + res.getArg(0));
		    Port p = (Port) res.getArg(0);
		    workers.add(p);
		    log("porta = " + p.toString());
		    Message m = new Message("ack_associate");
		    send(p, m);
		} else if (res.getType().equalsIgnoreCase("dissociate")) {
		    Port p = (Port) res.getArg(0);
		    workers.remove(p);
		    Message m = new Message("ack_dissociate");
		    send(p, m);
		} else if (res.getType().equalsIgnoreCase("client_queue")) {
		    send(computeAct, new Message("Client_Queue_Response",
			    workers));
		} else {
		    log("Messaggio non riconosciuto : " + res.toString());
		}
	    } catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

    protected void send(Port p, Message m) throws UnknownHostException,
	    IOException {
	super.send(p, m);
	log("message sent: " + m.toString());
    }

    protected Message receive() {
	Message res = super.receive();
	log("message received; " + res.toString());
	return res;
    }

    private void log(String string) {
	System.out.println("Master : " + string);

    }
}
