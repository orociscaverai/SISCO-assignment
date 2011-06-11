package nbody_distribuito.worker;

import nbody_distribuito.Constants;
import nbody_distribuito.FlagActor;
import pcd.actors.MessageDispatcher;
import pcd.actors.Port;

public class MainWorker {

    public static void main(String[] args) {

	new MainWorker().doJob();
    }

    protected void doJob() {
	MessageDispatcher.getInstance().start();
	//Worker w2 = new Worker(Constants.WORKER_ACTOR, Constants.WORKER_HANDLER_ACTOR,
	//	"192.168.100.100", null);
	
	
	Port serverPort = new Port(Constants.WORKER_HANDLER_ACTOR, Constants.SERVER_IP); 
	
	for (int i = 0 ; i <5 ; i++){
	Worker w = new Worker(Constants.WORKER_ACTOR,  serverPort);
	
	//TODO
	FlagActor stopFlag = new FlagActor("stopFlag");
	stopFlag.start();
	w.start();
	}
	//w2.start();
    }

}
