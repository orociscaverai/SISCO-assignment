package nbody_distribuito.controller;

import gui.NBodyView;

import java.io.IOException;
import java.net.UnknownHostException;

import nbody.event.ChangeParamEvent;
import nbody.event.Event;
import nbody.event.RandomizeEvent;
import nbody.event.StartedEvent;
import nbody_distribuito.Constants;
import pcd.actors.Message;
import pcd.actors.Port;

/**
 * Questa classe funge da ascoltatore di eventi provenienti dalla GUI. Dopo aver
 * determinato la tipologia dell'evento, vengono inviati i messaggi specifici ai
 * vari attori, svolgendo quindi il compito di un Controller.
 * 
 * La scelta di utilizzare gli eventi per l'interazione con la GUI, evitando una
 * soluzione a scambio di messaggio pura, deriva dalla volontà di riutilizzare
 * il codice scritto in precedenza. In questo caso quindi tale classe
 * rappresenta un "Attore Adattatore" tra la gestione ad eventi e la gestione a
 * scambio di messaggio.
 * 
 * */

public class EventHandler extends ControllerAgent {

    private Port computePort = new Port("Compute", "localhost");
    private String computeActorName, stopActorName, workerHandlerActorName;

    public EventHandler(String actorName, NBodyView view, String computeActorName,
	    String stopActorName, String workerHandlerActorName) {
	super(actorName);
	
	view.register(this);
	
	this.computeActorName = computeActorName;
	this.stopActorName = stopActorName;
	this.workerHandlerActorName = workerHandlerActorName;

    }

    private void init() {

    }

    @Override
    public void run() {
	try {
	    while (true) {
		Event ev;
		ev = fetchEvent();
		log("received ev: " + ev.getDescription());
		if (ev.getDescription().equals("randomize")) {

		    RandomizeEvent rev = (RandomizeEvent) ev;
		    int numBodies = rev.getNumBodies();
		    Message m = new Message(Constants.RANDOMIZE_EVENT, numBodies);
		    send(new Port(computeActorName), m);

		} else if (ev instanceof StartedEvent) {

		    Message m = new Message(Constants.START_EVENT);
		    send(new Port(computeActorName), m);

		} else if (ev.getDescription().equals("paused")) {

		    Message m = new Message(Constants.PAUSE_EVENT);

		} else if (ev.getDescription().equals("stopped")) {

		    Message m = new Message(Constants.STOP_EVENT);
		    send(new Port(stopActorName), m);

		} else if (ev.getDescription().equals("singleStep")) {

		    // TODO Effettua una sola computazione

		} else if (ev.getDescription().equals("changeParam")) {

		    float deltaTime = ((ChangeParamEvent) ev).getDeltaTime();
		    float softFactor = ((ChangeParamEvent) ev).getSoftFactor();
		    Message m = new Message(Constants.CHANGE_PARAM, deltaTime, softFactor);
		}
	    }

	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (UnknownHostException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
