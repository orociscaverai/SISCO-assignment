package nbody_distribuito.worker;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nbody_distribuito.Constants;
import nbody_distribuito.master.Job;
import nbody_distribuito.master.JobResult;
import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;
import pcd.actors.filters.MsgFilter;
import pcd.actors.filters.MsgFilterImpl;

public class Worker extends Actor {

	private Port masterPort, computePort;
	private ExecutorService ex;
	private ExecutorCompletionService<Boolean> compServ;

	public Worker(String actorName, String serverName, String serverAddress, MsgFilter filter) {
		super(actorName);

		// TODO inserire il metodo per ottenere l'IP
		this.setLocalPort(new Port(actorName, "192.168.100.101" ));
		masterPort = new Port(serverName, serverAddress);
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
		//il server mi manda la porta associata al compute Actor
		if (!m.getType().equalsIgnoreCase(Constants.ACK_ASSOCIATE)) {
			log("Il master non ha apprezzato la mia associazione");
			log("Il master ha risposto con: " + m.toString());
			return false;
		}
		computePort = (Port)m.getArg(0);
		log("nuova porta = "+computePort.getActorName() +" "+ computePort.getHostName());
		log("messaggio ricevuto, associaizione avvenuta " + m.toString());
		return true;
	}

	private void initPool() {
		int poolSize = Runtime.getRuntime().availableProcessors() * 3;
		ex = Executors.newFixedThreadPool(poolSize);
		this.compServ = new ExecutorCompletionService<Boolean>(ex);
	}

	private boolean init() {
		if (!associate()) {
			return false;
		}
		initPool();
		return true;

	}

	private void shutdownAndReset() throws InterruptedException {
		ex.shutdownNow();
		ex.awaitTermination(2, TimeUnit.MINUTES);

		int debug = 0;
		while (compServ.poll() != null) {
			debug++;
		}
		log("Numero di risultati già terminati: " + debug);

		initPool();
	}

	public JobResult doCompute(Job job, float deltaTime, float softFactor) throws Exception {

//		System.out.println("stampa primo set");
//		for(int i = 0; i<job.getFistSet().size();i++){
//			System.out.println(job.getFistSet().get(i).getId());
//
//		}
//		System.out.println("stampa secondo set ");
//		for(int i = 0; i<job.getSecondSet().size();i++){
//			System.out.println(job.getSecondSet().get(i).getId());
//		}
		JobResult j = new JobResult();

		float[] pippo = new float[2];
		pippo[0] = 1.3f;
		pippo[1] = 1.3f;
		j.addResult(job.getFistSet().get(0).getId(), pippo);
		return j;
		// int numBodies = bm.getNumBodies();
		// InteractionMatrix interactionMatrix = new
		// InteractionMatrix(numBodies);
		//
		// // Inizio la fase 1 -----------------------------------------
		// try {
		// for (int i = 0; i < numBodies - 1; i++) {
		// for (int j = i + 1; j < numBodies; j++) {
		// compServ.submit(new ComputeMutualAcceleration(i, j,
		// interactionMatrix, bm,
		// softFactor), true);
		// // log("submitted task " + i + " " + j);
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// // Combinazioni senza ripetizioni di tutti i Bodies
		// int numTask = (numBodies * (numBodies - 1)) / 2;
		//
		// // La creo qui per guadagnare tempo
		// BodiesMap newMap = new BodiesMap(numBodies);
		//
		// // Attendo i risultati della fase 1
		// try {
		// for (int n = 0; n < numTask; n++) {
		// compServ.take();
		// if (isStopped()) {
		// log("Stop alla Fase 1");
		// shutdownAndReset();
		// throw new Exception("Gestito lo stop della computazione");
		// }
		// }
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// // Mi assicuro di tornare ad uno stato consistente
		// shutdownAndReset();
		// throw new Exception("Gestita l'interrupt exception");
		// }
		//
		// // Inizio la fase 2 -----------------------------------------
		// for (int i = 0; i < numBodies; i++) {
		//
		// compServ.submit(new ComputeNewPosition(i, bm.getPosition(i),
		// deltaTime,
		// interactionMatrix, newMap), true);
		// // log("submitted task " + i + " " + j);
		// }
		//
		// // Attendo i risultati della fase 2
		// try {
		// for (int n = 0; n < numBodies; n++) {
		//
		// compServ.take();
		// if (isStopped()) {
		// log("Stop alla Fase 2");
		// shutdownAndReset();
		// throw new Exception("Stop Alla fase 2");
		//
		// }
		// }
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// // Mi assicuro di tornare ad uno stato consistente
		// shutdownAndReset();
		// throw new Exception("Gestita l'interrupt exception");
		// }
		//
		// return newMap;

	}// doCompute()

	//
	//
	//
	//
	//
	// public BodiesMap doComputeBack(BodiesMap bm, float deltaTime, float
	// softFactor) throws Exception {
	//
	// int numBodies = bm.getNumBodies();
	// InteractionMatrix interactionMatrix = new InteractionMatrix(numBodies);
	//
	// // Inizio la fase 1 -----------------------------------------
	// try {
	// for (int i = 0; i < numBodies - 1; i++) {
	// for (int j = i + 1; j < numBodies; j++) {
	// compServ.submit(new ComputeMutualAcceleration(i, j, interactionMatrix,
	// bm,
	// softFactor), true);
	// // log("submitted task " + i + " " + j);
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// // Combinazioni senza ripetizioni di tutti i Bodies
	// int numTask = (numBodies * (numBodies - 1)) / 2;
	//
	// // La creo qui per guadagnare tempo
	// BodiesMap newMap = new BodiesMap(numBodies);
	//
	// // Attendo i risultati della fase 1
	// try {
	// for (int n = 0; n < numTask; n++) {
	// compServ.take();
	// if (isStopped()) {
	// log("Stop alla Fase 1");
	// shutdownAndReset();
	// throw new Exception("Gestito lo stop della computazione");
	// }
	// }
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// // Mi assicuro di tornare ad uno stato consistente
	// shutdownAndReset();
	// throw new Exception("Gestita l'interrupt exception");
	// }
	//
	// // Inizio la fase 2 -----------------------------------------
	// for (int i = 0; i < numBodies; i++) {
	//
	// compServ.submit(new ComputeNewPosition(i, bm.getPosition(i), deltaTime,
	// interactionMatrix, newMap), true);
	// // log("submitted task " + i + " " + j);
	// }
	//
	// // Attendo i risultati della fase 2
	// try {
	// for (int n = 0; n < numBodies; n++) {
	//
	// compServ.take();
	// if (isStopped()) {
	// log("Stop alla Fase 2");
	// shutdownAndReset();
	// throw new Exception("Stop Alla fase 2");
	//
	// }
	// }
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// // Mi assicuro di tornare ad uno stato consistente
	// shutdownAndReset();
	// throw new Exception("Gestita l'interrupt exception");
	// }
	//
	// return newMap;
	//
	// }// doCompute()

	@Override
	public void run() {
		if (init()) {
			
			Message m = receive();
			if (m.getType().equals(Constants.DO_JOB)) {

				Job j = (Job) m.getArg(0);
				float deltaTime = (Float) m.getArg(1);
				float softFactor = (Float) m.getArg(2);
				log("finito step 1");
				try {
					JobResult jr = doCompute(j, deltaTime, softFactor);
					log("sending JOB_RESULT to "+computePort.getActorName()+ " "+ computePort.getHostName());
					send(computePort, new Message(Constants.JOB_RESULT, jr));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		log("Termino spontaneamente l'esecuzione");
	}

	private boolean isStopped() {

		Port stopFlag = new Port("stopFlag");

		try {
			send(stopFlag, new Message(Constants.IS_SET, getLocalPort()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Message m = receive(new MsgFilterImpl(Constants.IS_SET_RESULT, 1));
		Boolean b = (Boolean) m.getArg(0);
		return b.booleanValue();
	}

	private void log(String msg) {
		System.out.println(getActorName() + ": " + msg);
	}

}