package nbody_distribuito.master;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

import nbody_distribuito.BodiesMap;
import nbody_distribuito.Constants;
import nbody_distribuito.master.exception.StoppedException;
import nbody_distribuito.master.filter.QueueFilter;
import nbody_distribuito.message.ChangeParamMessage;
import nbody_distribuito.message.DoJobMessage;
import nbody_distribuito.message.JobResultMessage;
import nbody_distribuito.message.PauseComputationMessage;
import nbody_distribuito.message.RandomizeMessage;
import nbody_distribuito.message.SingleStepComputationMessage;
import nbody_distribuito.message.StartComputationMessage;
import nbody_distribuito.message.StopComputationMessage;
import nbody_distribuito.view.NBodyView;

import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;
import pcd.actors.filters.MsgFilter;

public class ComputeActor extends Actor {

    private Port workerHandler;
    private float deltaTime, softFactor;
    private BodiesMap map;

    private NBodyView view;

    public ComputeActor(String actorName, Port workerHandler, NBodyView view) {
	super(actorName);
	deltaTime = 0.5f;
	softFactor = 0.5f;
	this.workerHandler = workerHandler;
	this.view = view;
    }

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
	    if (res instanceof StartComputationMessage) {
		doStart();

	    } else if (res instanceof SingleStepComputationMessage) {
		doSingleStep();

	    } else if (res instanceof RandomizeMessage) {

		RandomizeMessage rm = ((RandomizeMessage) res);
		int numBodies = rm.getNumBodies();
		doRandomize(numBodies);

	    } else if (res instanceof ChangeParamMessage) {

		ChangeParamMessage cp = ((ChangeParamMessage) res);
		deltaTime = cp.getDeltaTime();
		softFactor = cp.getSoftFactor();

		log("messaggio non riconosciuto " + res.toString());
	    }
	}

    }

    private void doSingleStep() throws StoppedException {
	try {
	    float newDelta = deltaTime;
	    float newSoft = softFactor;

	    Vector<Port> workers = DoAcquireAvailableWorkers();

	    // Suddivido il carico di lavoro in base al numero di worker
	    // disponibili. Il tipo di strategia usata per suddividere il
	    // lavoro dipende dal tipo di implementazione realizzata
	    StrategyFactory sf = new StrategyFactory();
	    PartitionStrategy ps = sf.getStrategy();
	    ps.splitJob(workers.size(), map);

	    // send dei primi messaggi a tutti i worker
	    int waitCompleteJobs = 0;
	    int workerID = 0;
	    //mando 2 job ai worker per accelerare il lavoro..
	    for (int i=0;i<2;i++){
	    for (Port worker : workers) {
		Job n = ps.getNextJob();

		if (n != null) {
		    // il workerID è un identificativo valido per la
		    // computazione attuale, serve assegnare eventuali job ai
		    // primi worker che terminano.
		    Message m = new DoJobMessage(workerID, n, deltaTime, softFactor);
		    send(worker, m);
		    waitCompleteJobs++;
		    workerID = (workerID+1)%workers.size();
		}
	    }
	    }
	    

	    ResultAggregator computeResult = new ResultAggregator(map.getNumBodies(), deltaTime);
	    computeResult.initialize(map);

	    while (waitCompleteJobs > 0) {
		Message res = super.receive();
		if (res instanceof StopComputationMessage || res instanceof PauseComputationMessage) {
		    // this.getMessageBox().getQueue().clear();
		    throw new StoppedException("STOP!");
		    // La Mailbox sarà clearata in seguito or TODO switch
		    // mailbox per sicurezza
		} else if (res instanceof ChangeParamMessage) {
		    ChangeParamMessage cp = ((ChangeParamMessage) res);
		    newDelta = cp.getDeltaTime();
		    newSoft = cp.getSoftFactor();
		} else if (res instanceof JobResultMessage) {

		    JobResultMessage jr = ((JobResultMessage) res);

		    Job n = ps.getNextJob();
		    if (n != null) {
			int id = jr.getWorkerID();
			send(workers.elementAt(id), new DoJobMessage(id, n, deltaTime, softFactor));
		    } else {
			waitCompleteJobs--;
		    }

		    List<ClientResponse> resultJob = jr.getResult();

		    computeResult.aggregate(resultJob);

		} else {
		    log("messaggio non riconosciuto " + res.getType());
		}
	    }

	    map = computeResult.getResultMap();
	    deltaTime = newDelta;
	    softFactor = newSoft;
	    // log(map.toString());

	    view.setUpdated(map);

	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (NumWorkerException e) {
	    e.printStackTrace();
	}

    }

    private void doRandomize(int numBodies) {

	map = new BodiesMap();
	map.generateRandomMap(numBodies);
	view.setUpdated(map);
    }

    private void doStart() {
	try {
	    while (true) {
		doSingleStep();
	    }
	} catch (StoppedException se) {
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
	Message m;
	do {
	    m = super.receive();
	} while (m == null);

	log("message received; " + m.toString());
	return m;
    }

    private void log(String msg) {
	// synchronized (System.out) {
	// System.out.println(getActorName() + ": " + msg);
	// }
    }

    private void errorLog(String msg) {
	synchronized (System.out) {
	    System.out.println(getActorName() + ": " + msg);
	}
    }
}
