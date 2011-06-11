package nbody_distribuito.controller;

import nbody.view.swing.AbstractView;
import pcd.actors.Actor;
import pcd.actors.Message;

public class GuiUpdaterActor extends Actor {

    protected GuiUpdaterActor(String actorName, AbstractView view) {
	super(actorName);

    }

    @Override
    public void run() {
	while (true) {
	    Message m = receive();
	}
    }

}
