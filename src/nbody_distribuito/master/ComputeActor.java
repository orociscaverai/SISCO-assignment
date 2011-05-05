package nbody_distribuito.master;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import nbody_distribuito.BodiesMap;
import nbody_distribuito.Constants;
import nbody_distribuito.master.filter.QueueFilter;
import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;
import pcd.actors.filters.MsgFilter;

public class ComputeActor extends Actor {

    private Port masterPort = new Port("Master", "localhost");
    private int numBody;

    public ComputeActor(String actorName) {
	super(actorName);
    }

    @Override
    public void run() {
	
	while (true) {
	    
	    Message res = receive();
	    if (res.getType().equals(Constants.START_EVENT)) {

		doStart();
	    } else if (res.getType().equals(Constants.RANDOMIZE_EVENT)) {
		
		numBody = (Integer) res.getArg(0);
		doRandomize();
	    } else {
		
		log("messaggio non riconosciuto " + res.toString());
	    }
	}

    }

    private void doRandomize() {

	// TODO Auto-generated method stub
    }

    private void doStart() {
	
	while (true) {
	    try {
		PartitionStrategy ps = new MyStrategy();
		Tree tr = ps.partitionMap();
		
		send(masterPort, new Message(Constants.CLIENT_QUEUE));
		
		MsgFilter f = new QueueFilter();
		Message res = receive(f);
		Vector<Port> workers = (Vector<Port>) res.getArg(0);
		
		// send dei primi messaggi a tutti i worker
		int waitCompleteJobs = 0;
		for (int i = 0; i < workers.size(); i++) {
		    AbstractNode n = tr.navigateInOrder();
		    if (n == null)
			break;
		    BodiesMap map = (BodiesMap) n.getValue();
		    Port worker = workers.elementAt(i);
		    // FIXME mancano nell'ordine il deltaTIme e softFactor
		    send(worker, new Message(Constants.DO_JOB, map));
		    waitCompleteJobs++;
		}
		
		BodiesMap newMap = new BodiesMap(numBody);
		while (waitCompleteJobs != 0) {
		    res = receive();
		    if (res.getType().equalsIgnoreCase("stop")) {
			// this.getMessageBox().getQueue().clear();
			return;
			// La Mailbox sarà clearata in seguito or TODO switch
			// mailbox per sicurezza
		    } else if (res.getType().equalsIgnoreCase("ResultCompute")) {
			AbstractNode n = tr.navigateInOrder();
			if (n != null) {
			    // TODO per il send serve identificare l'attore che
			    // ha finito la computazione
			    // quindi aggiungere un id al messaggio
			    // ResultCompute
			    int id = 0;
			    BodiesMap map = (BodiesMap) n.getValue();
			    send(workers.elementAt(id), new Message("DoJob",
				    map));
			} else {
			    waitCompleteJobs--;
			}
			// TODO aggregazione della mappa
			BodiesMap resultMap = (BodiesMap) res.getArg(0);
			aggregateMap(newMap, resultMap);
		    } else {
			log("messaggio non riconosciuto " + res.toString());
		    }
		}
		// TODO tutti i job sono completati sendare newMap a qualche
		// attore?
	    } catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }

    private void aggregateMap(BodiesMap newMap, BodiesMap resultMap) {
	// TODO Auto-generated method stub

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

    protected Message receive(MsgFilter f) {
	Message res = super.receive(f);
	log("message received; " + res.toString());
	return res;
    }

    private void log(String string) {
	System.out.println("Compute Actor : " + string);
    }
}
