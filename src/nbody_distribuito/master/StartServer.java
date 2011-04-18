package nbody_distribuito.master;

import pcd.actors.Actor;
import pcd.actors.MessageDispatcher;

public class StartServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MessageDispatcher.getInstance().start();
		Actor master = new Master("Master");
		master.start();
		Actor computeActor = new ComputeActor("Compute");
		computeActor.start();
		Actor guiActor = new GuiActor("Gui");
		guiActor.start();
	}

}
