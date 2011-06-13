package nbody.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nbody.model.Bodies;
import nbody.model.BodiesMap;
import nbody.model.Body;
import nbody.model.InteractionMatrix;
import nbody.model.StateMonitor;
import nbody.model.StateVariables;
import nbody.view.AbstractView;

public class Master extends Thread {

	private InteractionMatrix interactionMatrix;
	private BodiesMap map;

	private AbstractView view;
	private StateVariables var;
	private StateMonitor state;

	private ExecutorService ex;
	private ExecutorCompletionService<Boolean> compServ;

	public Master(AbstractView view, StateMonitor state, StateVariables var) {
		super("Master");

		this.view = view;
		this.var = var;
		this.state = state;

		doReset();
	}

	/** Inizializza gli Executor */
	private void initPool() {
		int poolSize = Runtime.getRuntime().availableProcessors() + 1;
		ex = Executors.newFixedThreadPool(poolSize);
		this.compServ = new ExecutorCompletionService<Boolean>(ex);
	}

	/**
	 * Funzione utile in caso di stop dell'applicazione: si occupa di attendere
	 * l'arresto della computazione e di riportare lapplicazione in uno stato
	 * consistente
	 */
	private void shutdownAndReset() throws InterruptedException {
		ex.shutdownNow();
		ex.awaitTermination(2, TimeUnit.MINUTES);
		int debug = 0;
		while (compServ.poll() != null) {
			debug++;
		}
		log("Numero di risultati già terminati: " + debug);

		initPool();

		doReset();
	}

	/**
	 * Coordina le fasi necessarie per la computazione di un singolo step della
	 * simulazione. Il lavoro viene implementato mediante gli excuto di Java.
	 * Infine viene inviata la notifica alla Gui per effettuare l'aggiornamento
	 * della visualizzazione.
	 */
	private void doCompute() throws InterruptedException {

		int numBodies = map.getNumBodies();
		float deltaTime = var.getDeltaTime();
		float softFactor = var.getSoftFactor();

		// Inizio la fase 1
		try {
			for (int i = 0; i < numBodies - 1; i++) {
				for (int j = i + 1; j < numBodies; j++) {
					compServ.submit(new ComputeMutualAcceleration(i, j, interactionMatrix, map,
							softFactor), true);
					// log("submitted task " + i + " " + j);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Combinazioni senza ripetizioni di tutti i Bodies
		int numTask = (numBodies * (numBodies - 1)) / 2;

		// La creo qui per guadagnare tempo
		BodiesMap newMap = new BodiesMap(numBodies);
		try {
			for (int n = 0; n < numTask; n++) {
				compServ.take();
				if (state.isStopped()) {
					// Per lo stop viene usata la classe
					// ExecutorCompletionService
					log("Stop alla Fase 1");
					view.setState("Stopped");
					shutdownAndReset();
					return;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			// Mi assicuro di tornare ad uno stato consistente
			shutdownAndReset();
			return;
		}

		// Inizio la fase 2
		for (int i = 0; i < numBodies; i++) {

			compServ.submit(new ComputeNewPosition(i, map.getPosition(i), deltaTime,
					interactionMatrix, newMap), true);
			// log("submitted task " + i + " " + j);
		}

		try {
			for (int n = 0; n < numBodies; n++) {

				compServ.take();
				if (state.isStopped()) {
					// Per lo stop viene usata la classe
					// ExecutorCompletionService
					log("Stop alla Fase 2");
					view.setState("Stopped");
					shutdownAndReset();
					return;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			// Mi assicuro di tornare ad uno stato consistente
			shutdownAndReset();
			return;
		}

		// Posso usarla anche se l'ho inviata alla vista, dato che d'ora
		// in poi verrà acceduta solo in lettura
		map = newMap;

		view.setUpdated(newMap);
	}// doCompute()

	private void doReset() {
		var.setNumBodies(0);
		map = new BodiesMap(0);
		interactionMatrix = new InteractionMatrix(0);
	}

	private void doRandomize() throws InterruptedException {
		int numBodies = var.getNumBodies();
		this.map = new BodiesMap(numBodies);
		interactionMatrix = new InteractionMatrix(numBodies);
		Bodies.getInstance().makeRandomBodies(numBodies);

		map.generateRandomMap();
		view.setUpdated(map);

	}// doRandomize()

	public void run() {
		initPool();
		while (true) {
			try {
				int action = state.waitAction();
				if (action == 0 || action == 2) {
					if (action == 0){
						view.setState("Started - Processing");
					}else{
						view.setState("Stepping");
					}
					doCompute();
				} else if (action == 1) {
					view.setState("Randomized");
					doRandomize();
				}else if(action == 3){
					view.setState("Opening File..");
					doOpenFile();
					view.setState("File Opened");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				log("restartingTime " + System.currentTimeMillis());
				break;
			}
		}
	}

	private void doOpenFile() {
		File f = var.getFile();
		try {
			FileReader fileReader = new FileReader(f);
			BufferedReader fileBufferedReader = new BufferedReader(fileReader ); 
			Bodies.getInstance().clear();
			Vector<float[]> positions = new Vector<float[]>();
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
					Body b = new Body(mass, vel);
					Bodies.getInstance().addPlanet(b);
					positions.add(pos);

				}else{
					log("Skipping line - Wrong Format");
				}
				s = fileBufferedReader.readLine();
			}
			map = new BodiesMap(positions.size());
			for (int i = 0; i< positions.size(); i++){
				map.setPosition(i, positions.elementAt(i));
			}
			fileReader.close();
			var.setNumBodies(positions.size());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		view.setUpdated(map);

	}

	private void log(String error) {
		synchronized (System.out) {
			System.out.println("[MASTER] : " + error);
		}
	}
}
