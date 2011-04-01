package nbody_distribuito.master;

//PutFileServer Concorrente

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import nbody_distribuito.Message;

public class NetworkLayerMaster {

    BlockingQueue<ClientNetworkUtils> newClient;

    public NetworkLayerMaster() {
	newClient = new LinkedBlockingQueue<ClientNetworkUtils>();
    }

    private class ConnectionHandler extends Thread {

	ServerSocket masterSocket = null;

	public ConnectionHandler(int port) {

	    try {
		masterSocket = new ServerSocket(port);
		masterSocket.setReuseAddress(true);

		System.out.println("PutFileServerCon: avviato ");
		System.out.println("Server: creata la server socket: "
			+ masterSocket);
	    } catch (Exception e) {
		System.err
			.println("Server: problemi nella creazione della server socket: "
				+ e.getMessage());
		e.printStackTrace();
		System.exit(1);
	    }
	}

	public void run() {
	    try {

		while (true) {
		    System.out.println("Server: in attesa di richieste...\n");

		    try {
			// bloccante finchè non avviene una connessione
			Socket clientSocket = masterSocket.accept();
			clientSocket.setSoTimeout(60000);
			// qui non è più così indispensabile, ma è comunque
			// meglio
			// evitare che un thread si blocchi indefinitamente
			System.out.println("Server: connessione accettata: "
				+ clientSocket);

			ObjectInputStream ois = new ObjectInputStream(
				clientSocket.getInputStream());
			Message msg = ((Message) ois.readObject());

			if (msg.getMessageType() == Message.CONNECT) {
			    newClient.add(new ClientNetworkUtils(clientSocket));
			}
		    } catch (Exception e) {
			System.err
				.println("Server: problemi nella accettazione della connessione: "
					+ e.getMessage());
			e.printStackTrace();
			// il server continua a fornire il servizio
			// ricominciando
			// dall'inizo del ciclo
			continue;
		    }

		} // while
	    }
	    // qui catturo le eccezioni non catturate all'interno del while
	    // in seguito alle quali il server termina l'esecuzione
	    catch (Exception e) {
		e.printStackTrace();
		// chiusura di stream e socket
		System.out.println("PutFileServerCon: termino...");
		System.exit(2);
	    }

	}
    }
}
