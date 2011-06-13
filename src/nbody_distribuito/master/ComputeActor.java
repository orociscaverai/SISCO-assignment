package nbody_distribuito.master;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import nbody_distribuito.Constants;
import nbody_distribuito.master.exception.NumWorkerException;
import nbody_distribuito.master.exception.StoppedException;
import nbody_distribuito.master.filter.QueueFilter;
import nbody_distribuito.master.filter.StopFilter;
import nbody_distribuito.master.strategy.PartitionStrategy;
import nbody_distribuito.master.strategy.StrategyFactory;
import nbody_distribuito.message.ChangeParamMessage;
import nbody_distribuito.message.DoJobMessage;
import nbody_distribuito.message.JobResultMessage;
import nbody_distribuito.message.OpenFileMessage;
import nbody_distribuito.message.PauseComputationMessage;
import nbody_distribuito.message.RandomizeMessage;
import nbody_distribuito.message.SingleStepComputationMessage;
import nbody_distribuito.message.StartComputationMessage;
import nbody_distribuito.message.StopComputationMessage;
import nbody_distribuito.model.BodiesMap;
import nbody_distribuito.model.Body;
import nbody_distribuito.shared_object.ClientResponse;
import nbody_distribuito.shared_object.Job;
import nbody_distribuito.view.AbstractView;

import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;
import pcd.actors.filters.MsgFilter;

public class ComputeActor extends Actor {

	private Port workerHandler;
	private float deltaTime, softFactor;
	private BodiesMap map;

	private AbstractView view;

	public ComputeActor(String actorName, Port workerHandler, AbstractView view) {
		super(actorName);
		deltaTime = 0.5f;
		softFactor = 0.5f;
		this.workerHandler = workerHandler;
		this.view = view;
	}

	//    public ComputeActor(String actorName, Port workerHandler) {
	//	super(actorName);
	//	deltaTime = 0.5f;
	//	softFactor = 0.5f;
	//	this.workerHandler = workerHandler;
	//    }

	@Override
	public void run() {

		while (true) {

			Message res = receive();
			if (res instanceof StartComputationMessage) {
				//inizio la computazione 
				view.setState("Started - Processing");
				doStart();

			} else if (res instanceof SingleStepComputationMessage) {
				//eseguo un singolo step di computazione
				view.setState("Stepping");
				doSingleStep();

			} else if (res instanceof RandomizeMessage) {
				//genero una mappa di pianeti random
				RandomizeMessage rm = ((RandomizeMessage) res);
				int numBodies = rm.getNumBodies();
				view.setState("Randomized");
				doRandomize(numBodies);

			} else if (res instanceof ChangeParamMessage) {
				//cambio i parametri di computazione
				ChangeParamMessage cp = ((ChangeParamMessage) res);
				deltaTime = cp.getDeltaTime();
				softFactor = cp.getSoftFactor();

			}else if (res instanceof StopComputationMessage){
				//blocco la computazione
				view.setState("Stopped");
			}else if (res instanceof OpenFileMessage){
				//apro un file e ne leggo il contenuto
				view.setState("Opening file");
				doOpenFile(((OpenFileMessage) res).getFile());
				//contenuto letto e caricato aggiorno la vista
				view.setState("File loaded");
			}
		}

	}

	/**metodo che pare un file e ne carica il contenuto su una nuova mappa
	 * Il file deve contenere per ogni riga 5 numeri float
	 * Il numero delle righe individua il numero dei corpi caricati
	 * 1) massa
	 * 2) posizione ascisse
	 * 3) posizione ordinate
	 * 4) componente orizzontale velocità
	 * 5) componente verticale di velocità
	 * 
	 * @param file
	 */
	private void doOpenFile(File file) {
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader fileBufferedReader = new BufferedReader(fileReader ); 
			map = new BodiesMap();
			int bodyIndex = 0;
			String s = fileBufferedReader.readLine();
			while(s!=null){
				StringTokenizer tokens = new StringTokenizer(s , " ");
				int tokenCount = tokens.countTokens();
				//Massa posizione e velocità sono 5 numeri
				//se non ci sono skip della linea
				if (tokenCount == 5){
					String massStr = tokens.nextToken();
					float mass = Float.parseFloat(massStr);
					String posStr1 = tokens.nextToken();
					String posStr2 = tokens.nextToken();
					String velStr1 = tokens.nextToken();
					String velStr2 = tokens.nextToken();
					float[] pos = new float[2];
					float[] vel = new float[2];
					pos[0] = Float.parseFloat(posStr1);
					pos[1] = Float.parseFloat(posStr2);
					vel[0] = Float.parseFloat(velStr1);
					vel[1] = Float.parseFloat(velStr2);
					Body b = new Body(bodyIndex, mass,pos,vel);
					map.addBody(b);


				}else{
					log("Skipping line - Wrong Format");
				}
				//carico nuova riga e aggiorno l'index
				s = fileBufferedReader.readLine();
				bodyIndex++;
			}
			//chiudo il file
			fileReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		//aggiorno la grafica
		view.setUpdated(map);
	}

	/** metodo che esegue un singolo step di computazione
	 * 
	 * @throws StoppedException
	 */
	private void doSingleStep() throws StoppedException {
		try {
			float newDelta = deltaTime;
			float newSoft = softFactor;

			//chiedo al WorkerHandler il numero di Worker associati
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

			//creo una nuova mappa e la inizializzo coi valori che già conosco
			ResultAggregator computeResult = new ResultAggregator(map.getNumBodies(), deltaTime);
			computeResult.initialize(map);

			while (waitCompleteJobs > 0) {

				Message res = super.receive();
				//Qualche worker deve essere crashato oppure terminato ignoro il problema
				//in quanto è un solo work non molto influente per terminare la computazione
				if (res == null){
					break;
				}else if (res instanceof StopComputationMessage || res instanceof PauseComputationMessage) {
					//aggiorno lo stato
					if (res instanceof StopComputationMessage){
						view.setState("Stopped");
					}else{
						view.setState("Paused");
					}
					//Attendo la conclusione dei job in pending per cancellarli dalla mailbox prima di terminare
					MsgFilter f = new StopFilter();
					for(int i = 0; i<waitCompleteJobs; i++){
						super.receive(f);
					}
					//ora posso terminare 
					throw new StoppedException("STOP!");
				} else if (res instanceof ChangeParamMessage) {
					//cambio i parametri ma solo alla fine dello step di computazione
					ChangeParamMessage cp = ((ChangeParamMessage) res);
					newDelta = cp.getDeltaTime();
					newSoft = cp.getSoftFactor();
				} else if (res instanceof JobResultMessage) {
					//ricevo il risultato del job
					JobResultMessage jr = ((JobResultMessage) res);

					Job n = ps.getNextJob();
					if (n != null) {
						//mando immediatamente un nuovo job al worker se è presente
						int id = jr.getWorkerID();
						send(workers.elementAt(id), new DoJobMessage(id, n, deltaTime, softFactor));
					} else {
						//attendo la conclusione del lavoro
						waitCompleteJobs--;
					}
					//ottengo i risultati
					List<ClientResponse> resultJob = jr.getResult();
					//aggiorno la mappa coi nuovi risultati ottenuti
					computeResult.aggregate(resultJob);

				} else {
					log("messaggio non riconosciuto " + res.getType());
				}
			}
			//aggiorno la mappa con la nuova mappa calcolata
			map = computeResult.getResultMap();
			//aggiorni i parametri
			deltaTime = newDelta;
			softFactor = newSoft;
			// log(map.toString());

			//aggiorno la vista
			view.setUpdated(map);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumWorkerException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Questo metodo genera una mappa random a partire dal numero di pianeti in ingresso
	 * @param numBodies
	 */
	private void doRandomize(int numBodies) {

		map = new BodiesMap();
		map.generateRandomMap(numBodies);
		view.setUpdated(map);
	}

	/**
	 * Questo metodo Si occupa dello start
	 */
	private void doStart() {
		try {
			while (true) {
				doSingleStep();
			}
		} catch (StoppedException se) {
			//è arrivato lo stop non devo far nulla se non uscire dal ciclo
		}
	}

	/**
	 * Metodo che si occupa di chiedere e ricevere la lista dei Worker
	 * al WorkerHandler
	 * @return La lista di porte dei Workers associati
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private Vector<Port> DoAcquireAvailableWorkers() throws UnknownHostException, IOException {
		// Richiedo al WorkerHandlerActor, che gestisce l'associazione
		// dei worker, quali siano quelli associati.
		send(workerHandler, new Message(Constants.CLIENT_QUEUE));

		// Ricevo una struttura dati contenente le porte dei vari worker
		MsgFilter f = new QueueFilter();
		Message res = receive(f);
		Vector<Port> workers = (Vector<Port>) res.getArg(0);
		if (workers.size() == 0) {
			log("Attendo l'associazione dei worker");
			view.setState("attendo l'associazione di un Worker");
			send(workerHandler, new Message(Constants.WAIT_ASSOCIATE));
			res = receive(f);
			workers = (Vector<Port>) res.getArg(0);
			view.setState("Worker arrivato - Processing");
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
