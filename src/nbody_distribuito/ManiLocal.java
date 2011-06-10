package nbody_distribuito;

import gui.NBodyView;
import nbody_distribuito.controller.EventHandler;
import nbody_distribuito.master.ComputeActor;
import nbody_distribuito.master.WorkerHandlerActor;
import nbody_distribuito.worker.Worker;
import pcd.actors.Actor;
import pcd.actors.MessageDispatcher;
import pcd.actors.Port;

public class ManiLocal {
    public static void main(String[] args) {

	MessageDispatcher.getInstance().start();

	NBodyView view = new NBodyView(600, 600);

	Port workerHandlerActor = new Port(Constants.WORKER_HANDLER_ACTOR, Constants.SERVER_IP);
	Port stopActor = new Port(Constants.STOP_ACTOR, Constants.SERVER_IP);
	Port computeActor = new Port(Constants.COMPUTE_ACTOR, Constants.SERVER_IP);

	new ComputeActor(Constants.COMPUTE_ACTOR, workerHandlerActor).start();
	new FlagActor(Constants.STOP_ACTOR).start();
	new WorkerHandlerActor(Constants.WORKER_HANDLER_ACTOR, computeActor).start();

	Actor controller = new EventHandler(Constants.EVENT_CONTROLLER_ACTOR, view, computeActor,
		stopActor);
	controller.start();

	try {
	    Thread.sleep(1000);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	Port serverPort = new Port(Constants.WORKER_HANDLER_ACTOR, Constants.SERVER_IP);

	Worker w = new Worker(Constants.WORKER_ACTOR, serverPort);
	
	w.start();
	

    }
}
