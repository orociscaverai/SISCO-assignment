package nbody_distribuito.worker;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nbody_distribuito.Constants;
import nbody_distribuito.message.DoJobMessage;
import nbody_distribuito.message.JobResultMessage;
import nbody_distribuito.shared_object.ClientData;
import nbody_distribuito.shared_object.ClientResponse;
import nbody_distribuito.shared_object.Job;
import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;

public class Worker extends Actor {

    private Port masterPort, computePort;
    private ExecutorService ex;
    private ExecutorCompletionService<ClientResponse> compServ;

    public Worker(String actorName, Port serverPort) {
	super(actorName);

	this.setLocalPort(new Port(actorName, Constants.WORKER_IP));
	masterPort = serverPort;
    }

    private boolean associate() {
	Message m;
	// invio la richiesta per associarmi al Master
	try {
	    m = new Message(Constants.ASSOCIATE, this.getLocalPort());
	    log("Ho preparato il seguente messaggio: " + m.toString());
	    log("Lo invio a: " + masterPort.toString());
	    send(masterPort, m);

	    log("Invio messaggio: " + m);
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();

	}

	// ricevo il messaggio di risposta
	m = receive();
	// il server mi manda la porta associata al compute Actor
	if (!m.getType().equalsIgnoreCase(Constants.ACK_ASSOCIATE)) {
	    log("Il master non ha apprezzato la mia associazione");
	    log("Il master ha risposto con: " + m.toString());
	    return false;
	}
	//prendo la porta del computeActor
	computePort = (Port) m.getArg(0);
	log("nuova porta = " + computePort.getActorName() + " " + computePort.getHostName());
	log("messaggio ricevuto, associaizione avvenuta " + m.toString());
	return true;
    }

    private void initPool() {
	int poolSize = Runtime.getRuntime().availableProcessors() * 3;
	ex = Executors.newFixedThreadPool(poolSize);
	this.compServ = new ExecutorCompletionService<ClientResponse>(ex);
    }

    //metodo che inizializza il worker e fa l'associate
    private boolean init() {
	if (!associate()) {
	    return false;
	}
	initPool();
	return true;

    }

    private void shutdownAndReset(){
	ex.shutdownNow();
	try {
		ex.awaitTermination(2, TimeUnit.MINUTES);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	int debug = 0;
	while (compServ.poll() != null) {
	    debug++;
	}
	log("Numero di risultati già terminati: " + debug);

	initPool();

    }

    /**
     * metodo che si occupa della computazione
     * @param job
     * @param deltaTime
     * @param softFactor
     * @return
     */
    private List<ClientResponse> doCompute(Job job, float deltaTime, float softFactor) {
	int numBodies = job.getNumBodies();
	InteractionMatrix interactionMatrix = new InteractionMatrix(numBodies);
	int numTask = job.getNumTask();
	for (int i = 0; i < numTask; i++) {
		//prendo i dati della prossima interazione
	    ClientData[] nextData = job.getDataOfNextInteraction();
	    //invio il task
	    compServ.submit(new ComputeMutualAcceleration(nextData[0], nextData[1],
		    interactionMatrix, softFactor), null);
	}
	// log("numTask = " + numTask);
	//attendo i risultati
	for (int n = 0; n < numTask; n++) {
	    try {
		compServ.take();
	    } catch (InterruptedException e) {
		shutdownAndReset();
		e.printStackTrace();
	    }
	}
	// log("inizio step 2");
	//per ogni pianeta un nuovo task
	for (int i = 0; i < numBodies; i++) {
	    ClientData c = job.getData(i);
	    //calcolo posizione e velocità
	    compServ.submit(new ComputeNewPosition(c, deltaTime, interactionMatrix));
	}
	// log("fine step 2");
	//creo la lista per il messaggio di risposta
	List<ClientResponse> response = new ArrayList<ClientResponse>(numBodies);
	for (int i = 0; i < numBodies; i++) {
	    try {
	    //raccolgo i risultati e li aggiungo al messaggio di risposta
		ClientResponse cr = compServ.take().get();
		response.add(cr);
	    } catch (InterruptedException e) {
	    	shutdownAndReset();
	    	e.printStackTrace();
	    } catch (ExecutionException e) {
	    	shutdownAndReset();
	    	e.printStackTrace();
	    }
	}
	// log("ritorno:\n");

	//restituisco la risposta
	return response;
    }

    @Override
    public void run() {
    //faccio l'associazione e inizializzo le mie variabili
	if (init()) {
	    while (true) {
		Message m = receive();
		if (m instanceof DoJobMessage) {
			//inizio il lavoro
		    //log("Ricevuto messaggio Do_JOB");
		    DoJobMessage dj = ((DoJobMessage) m);

		    Job j = dj.getJob();
		    float deltaTime = dj.getDeltaTime();
		    float softFactor = dj.getSoftFactor();

		    //Avvio la computazione
		    List<ClientResponse> jr = doCompute(j, deltaTime, softFactor);
		    log("sending JOB_RESULT to " + computePort.getActorName() + " "
			    + computePort.getHostName());
		    // for (ClientResponse r : jr) {
		    // // System.out.println("\n" + r.toString());
		    // }
		    try {
		    //mando il risultato al ComputeActor
			send(computePort, new JobResultMessage(dj.getWorkerID(), jr));

		    } catch (UnknownHostException e) {
			e.printStackTrace();
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
	    }

	}

	log("Termino spontaneamente l'esecuzione");
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
	// System.out.println(getActorName() + ": " + msg);
    }

}