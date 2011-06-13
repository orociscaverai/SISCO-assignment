package nbody_distribuito.master;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import nbody_distribuito.Constants;

import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;

public class WorkerHandlerActor extends Actor {
	/**
	 * Classe che si occupa della gestione dei workers
	 */
	private Vector<Port> workers;
	private Port computeActor;
	private boolean isWaiting;
	private Vector<Port> waitRemove;

	public WorkerHandlerActor(String actorName, Port computeActor) {
		super(actorName);
		this.computeActor = computeActor;
		workers = new Vector<Port>();
		waitRemove = new Vector<Port>();
		isWaiting = false;
	}

	@Override
	public void run() {
		while (true) {
			try {
				log("wait");
				Message res = receive();
				if (res.getType().equalsIgnoreCase(Constants.ASSOCIATE)) {
					//è arrivato un messaggio di associate aggiungo il worker alla lista
					Port p = (Port) res.getArg(0);
					log("porta " + p.getActorName() + " " + p.getHostName());
					workers.add(p);

					//invio messaggio di conferma
					Message m = new Message(Constants.ACK_ASSOCIATE, computeActor);
					send(p, m);
					// se il Compute Actor sta aspettando l'associazione di un
					// client
					// notificargli immediatamente l'avvenuta associazione
					if (isWaiting) {
						Message m1 = new Message(Constants.CLIENT_QUEUE_RESP, workers);
						send(computeActor, m1);
						isWaiting = false;
					}

				} else if (res.getType().equalsIgnoreCase(Constants.DISSOCIATE)) {
					//in caso di dissociazione non mandare subito la conferma
					//attendere il prossimo step
					Port p = (Port) res.getArg(0);
					waitRemove.add(p);

				} else if (res.getType().equalsIgnoreCase(Constants.CLIENT_QUEUE)) {
					//siamo al prossimo step posso eliminare i worker in attesa di dissociazione
					for(Port port : waitRemove){
						workers.remove(port);
						//mando il messaggio di ack per la dissociazione
						Message m = new Message(Constants.ACK_DISSOCIATE);
						send(port, m);
					}
					//tutti i worker in attesa sono stati rimossi pulisco la lista
					waitRemove.clear();
					
					//invio il messaggio al ComputeActor contenente i workers disponibili
					Message m = new Message(Constants.CLIENT_QUEUE_RESP, workers);
					send(computeActor, m);

				} else if (res.getType().equalsIgnoreCase(Constants.WAIT_ASSOCIATE)) {
					//il ComputeActor sta attendendo una associate ne tengo traccia
					isWaiting = true;
				} else {

					log("Messaggio non riconosciuto : " + res.toString());
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

	private void log(String string) {
		//System.out.println(this.getActorName() + " : " + string);

	}
}
