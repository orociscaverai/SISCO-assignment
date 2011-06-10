package nbody_distribuito.controller;

import java.io.IOException;
import java.net.UnknownHostException;

import nbody_distribuito.event.ChangeParamEvent;
import nbody_distribuito.event.Event;
import nbody_distribuito.event.RandomizeEvent;
import nbody_distribuito.event.StartedEvent;
import nbody_distribuito.message.ChangeParamMessage;
import nbody_distribuito.message.PauseComputationMessage;
import nbody_distribuito.message.RandomizeMessage;
import nbody_distribuito.message.SingleStepComputationMessage;
import nbody_distribuito.message.StartComputationMessage;
import nbody_distribuito.message.StopComputationMessage;
import nbody_distribuito.view.NBodyView;
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

    private Port computeActor;
    private Port stopActor;

    public EventHandler(String actorName, NBodyView view, Port computeActor, Port stopActor) {
	super(actorName);

	this.computeActor = computeActor;
	this.stopActor = stopActor;

	view.register(this);

    }

    @Override
    public void run() {

	// Creo gli attori che mi serviranno per realizzare la computazione
	// FIXME: da come era parso dalla teoria è gisto che questi attori
	// vengano creati dal primo attore coordinatore e non nel metodo main.
	// Valutare insieme la cosa.

	try {
	    while (true) {
		Event ev;
		ev = fetchEvent();
		log("received ev: " + ev.getDescription());
		if (ev.getDescription().equals("randomize")) {

		    RandomizeEvent rev = (RandomizeEvent) ev;
		    int numBodies = rev.getNumBodies();
		    Message m = new RandomizeMessage(numBodies);
		    send(computeActor, m);

		} else if (ev instanceof StartedEvent) {

		    Message m = new StartComputationMessage();
		    send(computeActor, m);

		} else if (ev.getDescription().equals("paused")) {

		    Message m = new PauseComputationMessage();
		    send(computeActor, m);

		} else if (ev.getDescription().equals("stopped")) {

		    Message m = new StopComputationMessage();
		    send(stopActor, m);
		    // TODO prova per vedere se va
		    send(computeActor, m);

		} else if (ev.getDescription().equals("singleStep")) {

		    Message m = new SingleStepComputationMessage();
		    send(computeActor, m);

		} else if (ev.getDescription().equals("changeParam")) {

		    float deltaTime = ((ChangeParamEvent) ev).getDeltaTime();
		    float softFactor = ((ChangeParamEvent) ev).getSoftFactor();
		    Message m = new ChangeParamMessage(deltaTime, softFactor);
		    send(computeActor, m);
		}
	    }

	} catch (InterruptedException e) {
	    e.printStackTrace();
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
