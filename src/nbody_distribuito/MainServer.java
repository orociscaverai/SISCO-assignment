package nbody_distribuito;

import gui.NBodyView;
import nbody_distribuito.controller.EventHandler;
import nbody_distribuito.master.ComputeActor;
import nbody_distribuito.master.WorkerHandlerActor;

import pcd.actors.Actor;
import pcd.actors.MessageDispatcher;

public class MainServer {

    /**
     * @param args
     */
    public static void main(String[] args) {
	MessageDispatcher.getInstance().start();

	Actor computeActor = new ComputeActor("compute");
	computeActor.start();
	Actor stopActor = new FlagActor("stop");
	stopActor.start();
	Actor workerHandActor = new WorkerHandlerActor("workerHandler");
	workerHandActor.start();

	NBodyView view = new NBodyView(600, 600);

	Actor controller = new EventHandler("controller", view, "compute", "stop", "workerHandler");
	controller.start();

    }
}
