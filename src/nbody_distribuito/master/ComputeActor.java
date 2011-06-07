package nbody_distribuito.master;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

import nbody_distribuito.BodiesMap;
import nbody_distribuito.Constants;
import nbody_distribuito.master.filter.QueueFilter;

import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;
import pcd.actors.filters.MsgFilter;

public class ComputeActor extends Actor {

    private Port workerHandler;
    private float deltaTime, softFactor;
    private BodiesMap map;

    public ComputeActor(String actorName, Port workerHandler) {
	super(actorName);
	deltaTime = 0.5f;
	softFactor = 0.5f;
	this.workerHandler = workerHandler;
    }

    @Override
    public void run() {

	while (true) {

	    Message res = receive();
	    if (res.getType().equals(Constants.START_EVENT)) {

		doStart();

	    } else if (res.getType().equals(Constants.RANDOMIZE_EVENT)) {

		int numBodies = (Integer) res.getArg(0);
		doRandomize(numBodies);

	    } else if (res.getType().equals(Constants.CHANGE_PARAM)) {

		deltaTime = (Float) res.getArg(0);
		softFactor = (Float) res.getArg(1);

	    } else {

		log("messaggio non riconosciuto " + res.toString());
	    }
	}

    }

    private void doRandomize(int numBodies) {

	map = new BodiesMap();
	map.generateRandomMap(numBodies);
    }

    private void doStart() {

	while (true) {
	    try {

		Vector<Port> workers = DoAcquireAvailableWorkers();

		// Suddivido il carico di lavoro in base al numero di worker
		// disponibili. Il tipo di strategia usata per suddividere il
		// lavoro dipende dal tipo di implementazione realizzata
		PartitionStrategy ps = new SplitStrategyUsingNewJob();
		ps.splitJob(workers.size(), map);

		// send dei primi messaggi a tutti i worker
		int waitCompleteJobs = 0;
		for (Port worker : workers) {
		    Job n = ps.getNextJob();

		    if (n != null) {
			send(worker, new Message(Constants.DO_JOB, n, deltaTime, softFactor));
			waitCompleteJobs++;
		    }
		}

		ResultAggregator computeResult = new ResultAggregator(map.getNumBodies(), deltaTime);
		computeResult.initialize(map);

		while (waitCompleteJobs > 0) {
		    Message res = receive();
		    if (res.getType().equalsIgnoreCase("stop")) {
			// this.getMessageBox().getQueue().clear();
			return;
			// La Mailbox sarà clearata in seguito or TODO switch
			// mailbox per sicurezza
		    } else if (res.getType().equalsIgnoreCase(Constants.JOB_RESULT)) {
			Job n = ps.getNextJob();
			if (n != null) {
			    // TODO per il send serve identificare l'attore che
			    // ha finito la computazione
			    // quindi aggiungere un id al messaggio
			    // ResultCompute
			    int id = 0;
			    send(workers.elementAt(id), new Message(Constants.DO_JOB, n, deltaTime,
				    softFactor));
			} else {
			    waitCompleteJobs--;
			}
			// TODO aggregazione della mappa
			List<ClientResponse> resultJob = (List<ClientResponse>) res.getArg(0);
			computeResult.aggregate(resultJob);
		    } else {
			log("messaggio non riconosciuto " + res.getType());
		    }
		}
		// TODO tutti i job sono completati sendare newMap a qualche
		// attore?

		map = computeResult.getResultMap();
		log(map.toString());

	    } catch (UnknownHostException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    } catch (NumWorkerException e) {
		e.printStackTrace();
	    }

	}
    }

    private Vector<Port> DoAcquireAvailableWorkers() throws UnknownHostException, IOException {
	// Richiedo al WorkerHandlerActor, che gestisce l'associazione
	// dei worker, quali siano quelli associati.
	send(workerHandler, new Message(Constants.CLIENT_QUEUE));

	// Ricevo una struttura dati contenente le porte dei vari worker
	MsgFilter f = new QueueFilter();
	Message res = receive(f);
	Vector<Port> workers = (Vector<Port>) res.getArg(0);
	if (workers.size() == 0) {
	    // TODO devo avvisare la gui dell'attesa dei worker
	    log("Attendo l'associazione dei worker");
	    send(workerHandler, new Message(Constants.WAIT_ASSOCIATE));
	    res = receive(f);
	    workers = (Vector<Port>) res.getArg(0);
	    log("Si è associato almeno un worker, inizio la computazione");
	}
	return workers;
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

    private void log(String msg) {
	System.out.println(getActorName() + ": " + msg);
    }
}
