package nbody_distribuito.worker;

import nbody_distribuito.Constants;
import nbody_distribuito.FlagActor;
import pcd.actors.MessageDispatcher;

public class MainWorker {

    public static void main(String[] args) {

	new MainWorker().doJob();
    }

    protected void doJob() {
	MessageDispatcher.getInstance().start();
	//Worker w2 = new Worker(Constants.WORKER_ACTOR, Constants.WORKER_HANDLER_ACTOR,
	//	"192.168.100.100", null);
	Worker w = new Worker(Constants.WORKER_ACTOR, Constants.WORKER_HANDLER_ACTOR,
		"192.168.100.100", null);
	FlagActor stopFlag = new FlagActor("stopFlag");
	stopFlag.start();
	w.start();
	//w2.start();
    }

}
