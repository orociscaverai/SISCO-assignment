package nbody_distribuito.controller;

import gui.NBodyView;
import pcd.actors.Actor;
import pcd.actors.Message;

public class GuiUpdaterActor extends Actor {

    protected GuiUpdaterActor(String actorName, NBodyView view) {
	super(actorName);

    }

    @Override
    public void run() {
	while (true) {
	    Message m = receive();
	}
    }

}
