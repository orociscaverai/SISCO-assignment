package nbody_distribuito.master;

import nbody_distribuito.Constants;
import nbody_distribuito.controller.EventHandler;
import nbody_distribuito.view.NBodyView;
import pcd.actors.Actor;
import pcd.actors.MessageDispatcher;
import pcd.actors.Port;

public class MainServer {

    /**
     * @param args
     */
    public static void main(String[] args) {

	MessageDispatcher.getInstance().start();

	NBodyView view = new NBodyView(600, 600);

	Port workerHandlerActor = new Port(Constants.WORKER_HANDLER_ACTOR, Constants.SERVER_IP);
	Port computeActor = new Port(Constants.COMPUTE_ACTOR, Constants.SERVER_IP);

	new ComputeActor(Constants.COMPUTE_ACTOR, workerHandlerActor, view).start();
	new WorkerHandlerActor(Constants.WORKER_HANDLER_ACTOR, computeActor).start();

	Actor controller = new EventHandler(Constants.EVENT_CONTROLLER_ACTOR, view, computeActor);
	controller.start();

    }
}
