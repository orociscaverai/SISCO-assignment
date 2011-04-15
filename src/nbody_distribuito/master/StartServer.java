package nbody_distribuito.master;

import pcd.actors.MessageDispatcher;

public class StartServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MessageDispatcher.getInstance().start();
		new Master("Master").start();
	}

}
