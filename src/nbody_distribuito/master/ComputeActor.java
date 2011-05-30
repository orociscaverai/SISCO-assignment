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
    private float deltaTime, softFactor;
    private BodiesMap map;

    public ComputeActor(String actorName) {
	super(actorName);
	deltaTime = 0.5f;
	softFactor = 0.5f;
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

	    } else if (res.getType().equals(Constants.CHANGE_PARAM)) {

		deltaTime = (Float) res.getArg(0);
		softFactor = (Float) res.getArg(1);

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
		// Richiedo al WorkerHandlerActor, che gestisce l'associazione
		// dei worker, quali siano quelli associati.
		send(masterPort, new Message(Constants.CLIENT_QUEUE));
		
		// Ricevo una struttura dati contenente le porte dei vari worker
		MsgFilter f = new QueueFilter();
		Message res = receive(f);
		Vector<Port> workers = (Vector<Port>) res.getArg(0);

		
		
		
		
		// Suddivido il carico di lavoro in base al numero di worker
		// disponibili. Il tipo di strategia usata per suddividere il
		// lavoro dipende dal tipo di implementazione realizzata
		PartitionStrategy ps = new SimpleSplitStrategy();
		ps.splitJob(workers.size(), map);

		// send dei primi messaggi a tutti i worker
		int waitCompleteJobs = 0;
		for (int i = 0; i < workers.size(); i++) {
		    Job n = ps.getNextJob();
		    if (n == null)
			break;
		    Port worker = workers.elementAt(i);

		    send(worker, new Message(Constants.DO_JOB, n, deltaTime, softFactor));
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
			Job n = ps.getNextJob();
			if (n != null) {
			    // TODO per il send serve identificare l'attore che
			    // ha finito la computazione
			    // quindi aggiungere un id al messaggio
			    // ResultCompute
			    int id = 0;
			    send(workers.elementAt(id), new Message(Constants.DO_JOB, n, deltaTime, softFactor));
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

    protected void send(Port p, Message m) throws UnknownHostException, IOException {
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
