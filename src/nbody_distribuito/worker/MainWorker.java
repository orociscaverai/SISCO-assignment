package nbody_distribuito.worker;

import nbody_distribuito.Constants;
import pcd.actors.MessageDispatcher;
import pcd.actors.Port;

public class MainWorker {

    public static void main(String[] args) {

	new MainWorker().doJob();
    }

    protected void doJob() {
	MessageDispatcher.getInstance().start();
	
	
	Port serverPort = new Port(Constants.WORKER_HANDLER_ACTOR, Constants.SERVER_IP); 
	
	for (int i = 0 ; i <5 ; i++){
	Worker w = new Worker(Constants.WORKER_ACTOR,  serverPort);
	
	w.start();
	}
    }

}
