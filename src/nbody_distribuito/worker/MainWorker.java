package nbody_distribuito.worker;

import nbody_distribuito.FlagActor;
import pcd.actors.MessageDispatcher;

public class MainWorker {

    public static void main(String[] args) {

	new MainWorker().doJob();
    }

    protected void doJob() {
	MessageDispatcher.getInstance().start();
	Worker w = new Worker("Worker", "Master", "localhost", null);
	FlagActor stopFlag = new FlagActor("stopFlag");
	stopFlag.start();
	w.start();
    }

}
