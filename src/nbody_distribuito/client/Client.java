package nbody_distribuito.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import nbody_distribuito.Message;

public class Client {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Client(int port) {
	try {
	    socket = new Socket("localhost", port);
	    System.out.println("Client: avviato ");
	    ois = new ObjectInputStream(socket.getInputStream());
	    oos = new ObjectOutputStream(socket.getOutputStream());

	} catch (IOException e) {
	    System.err
		    .println("Client: problemi nella creazione della socket: "
			    + e.getMessage());
	    e.printStackTrace();
	    System.exit(1);
	}

    }

    public void connect() {
	sendMessage(new Message(Message.CONNECT));
    }

    public Message receiveMessage() {
	try {
	    return (Message) ois.readObject();
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private void sendMessage(Message m) {
	try {
	    oos.writeObject(m);
	} catch (IOException e) {
	    // TODO Valutare se i try catch debbano essere gestiti qui o
	    // mediante throw
	    System.err.println("Client: problemi nell'invio del messaggio: "
		    + e.getMessage());
	    e.printStackTrace();
	    System.exit(1);
	}
    }

}
