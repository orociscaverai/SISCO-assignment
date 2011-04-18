package nbody_distribuito.master;

import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;

public class GuiActor extends Actor {
	private Port computePort = new Port("Compute","localhost");


	protected GuiActor(String actorName) {
		super(actorName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {

		while(true){
			Message m = receive();
			
		}
	}

}
