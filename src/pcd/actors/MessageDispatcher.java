package pcd.actors;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MessageDispatcher extends Thread {
    private HashMap<String, MessageBox> boxes = null;
    private ServerSocket mainSocket = null;
    private ObjectInputStream inStream = null;
    private static MessageDispatcher msgDispatcher = null;

    private MessageDispatcher() {
	boxes = new HashMap<String, MessageBox>();

	try {
	    this.mainSocket = new ServerSocket(20504);
	    mainSocket.setReuseAddress(true);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static MessageDispatcher getInstance() {
	if (msgDispatcher == null)
	    msgDispatcher = new MessageDispatcher();

	return msgDispatcher;
    }

    public void register(String actorName, MessageBox actorBox) {
	boxes.put(actorName, actorBox);
    }

    public void run() {
	while (true) {
	    try {
		Socket socket = mainSocket.accept();
		inStream = new ObjectInputStream(socket.getInputStream());

		String actorName = (String) inStream.readObject();
		Message message = (Message) inStream.readObject();

		MessageBox boxActor = boxes.get(actorName);

		boxActor.insert(message);

		inStream.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    }
	}
    }
    
    protected void finalize(){
	try {
	    mainSocket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
}
