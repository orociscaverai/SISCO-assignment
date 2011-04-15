package nbody_distribuito.worker;

import pcd.actors.MessageDispatcher;

public class MainWorker {

    public static void main(String[] args) {
	MessageDispatcher.getInstance().start();

	Worker w = new Worker("Worker", "Master", "192.168.100.101", null);
	w.start();

    }

}
