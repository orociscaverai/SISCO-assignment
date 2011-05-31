package nbody_distribuito;

import gui.NBodyView;
import nbody_distribuito.controller.EventHandler;
import pcd.actors.Actor;
import pcd.actors.MessageDispatcher;

public class MainServer {

    /**
     * @param args
     */
    public static void main(String[] args) {
	
	MessageDispatcher.getInstance().start();

	NBodyView view = new NBodyView(600, 600);

	Actor controller = new EventHandler(Constants.EVENT_CONTROLLER_ACTOR, view);
	controller.start();

    }
}
