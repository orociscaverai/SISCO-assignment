package nbody_distribuito;

import nbody_distribuito.controller.EventHandler;
import nbody_distribuito.master.ComputeActor;
import nbody_distribuito.master.WorkerHandlerActor;
import nbody_distribuito.view.PerformanceTest;
import nbody_distribuito.worker.Worker;
import pcd.actors.Actor;
import pcd.actors.MessageDispatcher;
import pcd.actors.Port;

public class PerformanceMainLocal {
    public static void main(String[] args) {

	MessageDispatcher.getInstance().start();

	PerformanceTest view = new PerformanceTest();

	Port workerHandlerActor = new Port(Constants.WORKER_HANDLER_ACTOR, Constants.SERVER_IP);
	Port computeActor = new Port(Constants.COMPUTE_ACTOR, Constants.SERVER_IP);

	new ComputeActor(Constants.COMPUTE_ACTOR, workerHandlerActor, view).start();
	new WorkerHandlerActor(Constants.WORKER_HANDLER_ACTOR, computeActor).start();

	Actor controller = new EventHandler(Constants.EVENT_CONTROLLER_ACTOR, view, computeActor);
	controller.start();

	try {
	    Thread.sleep(1000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	Port serverPort = new Port(Constants.WORKER_HANDLER_ACTOR, Constants.SERVER_IP);

	for (int i = 0; i < 1; i++) {
	    Worker w = new Worker(Constants.WORKER_ACTOR, serverPort);

	    w.start();
	}

	new Thread(view).start();
    }
}
