package nbody_distribuito.client;

import pcd.actors.Actor;

public class Client extends Actor {
    private String serverAddress;

    public Client(String actorName, String serverAddress) {
	super(actorName);
	this.serverAddress = serverAddress;
    }

    private void connect() {
    }

    @Override
    public void run() {

    }
}
