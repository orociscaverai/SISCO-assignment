package pcd.actors;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import pcd.actors.filters.MsgFilter;
import pcd.actors.guards.Guard;

public abstract class Actor extends Thread {
    private String actorName;
    private MessageBox messageBox = null;
    private MessageDispatcher msgDispatcher = null;
    private Port localPort = null;
    private Object token = null;

    protected Actor(String actorName) {
	this.setActorName(actorName);
	token = new Object();
	setMessageBox(new MessageBox(this, token));
	this.setMsgDispatcher(MessageDispatcher.getInstance());
	setLocalPort(new Port(actorName));
	getMsgDispatcher().register(actorName, getMessageBox());
    }

    private void setActorName(String actorName) {
	this.actorName = actorName;
    }

    protected String getActorName() {
	return actorName;
    }

    protected void setMessageBox(MessageBox messageBox) {
	this.messageBox = messageBox;
    }

    protected MessageBox getMessageBox() {
	return messageBox;
    }

    protected void setMsgDispatcher(MessageDispatcher msgDispatcher) {
	this.msgDispatcher = msgDispatcher;
    }

    protected MessageDispatcher getMsgDispatcher() {
	return msgDispatcher;
    }

    protected void setLocalPort(Port localPort) {
	this.localPort = localPort;
    }

    protected Port getLocalPort() {
	return localPort;
    }

    protected void send(Port port, Message message) throws UnknownHostException, IOException {
	Socket socket = new Socket(port.getHostName(), 20504);
	ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
	outStream.writeObject(port.getActorName());
	outStream.writeObject(message);
	outStream.close();
    }

    protected Message receive() {
	return messageBox.get();
    }

    protected synchronized Message receive(MsgFilter filter) {
	while (true) {
	    for (Message msg : messageBox.getQueue()) {
		if (filter.match(msg)) {
		    messageBox.remove(msg);
		    return msg;
		}
	    }

	    try {
		synchronized (token) {
		    System.out.println(this.getActorName() + " - mi metto in attesa ...");
		    token.wait();
		    System.out.println(this.getActorName() + " - mi risveglio ...");
		}
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }

    protected synchronized Choice receive(List<Guard> guardList) {
	for (Guard guard : guardList) {
	    if (guard.evaluate()) {
		while (true) {
		    for (Message msg : messageBox.getQueue()) {
			if (guard.getFilter().match(msg)) {
			    Choice choice = guard.getChoice();
			    choice.setMessage(msg);
			    messageBox.remove(msg);
			    return choice;
			}
		    }

		    try {
			synchronized (token) {
			    System.out.println(this.getActorName() + " - mi metto in attesa ...");
			    token.wait();
			    System.out.println(this.getActorName() + " - mi risveglio ...");
			}
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    }
	}
	return null;
    }

    public abstract void run();

}
