package nbody_distribuito.master;

//PutFileServer Concorrente

import java.io.*;
import java.net.*;

public class NetworkLayerMaster extends Thread {

    public NetworkLayerMaster(int port) {

	ServerSocket masterSocket = null;

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

	try {

	    while (true) {
		System.out.println("Server: in attesa di richieste...\n");

		try {
		    // bloccante finchè non avviene una connessione
		    Socket workerSocket = masterSocket.accept();
		    workerSocket.setSoTimeout(60000);
		    // qui non è più così indispensabile, ma è comunque meglio
		    // evitare che un thread si blocchi indefinitamente
		    System.out.println("Server: connessione accettata: "
			    + workerSocket);
		    
		    // Avvio il nuovo thread
		    new PutFileServerThread(workerSocket).start();
		} catch (Exception e) {
		    System.err
			    .println("Server: problemi nella accettazione della connessione: "
				    + e.getMessage());
		    e.printStackTrace();
		    // il server continua a fornire il servizio ricominciando
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
} // PutFileServerCon

// Thread lanciato per ogni richiesta accettata
// versione per il trasferimento di file binari
class PutFileServerThread extends Thread {

    private Socket clientSocket = null;

    // costruttore
    public PutFileServerThread(Socket clientSocket) {
	this.clientSocket = clientSocket;
    }

    /**
     * Main invocabile con 0 o 1 argomenti. Argomento possibile -> porta su cui
     * il server ascolta.
     * 
     */
    public void run() {
	DataInputStream inSock;
	DataOutputStream outSock;
	try {
	    try {
		// creazione stream di input e out da socket
		inSock = new DataInputStream(clientSocket.getInputStream());
		outSock = new DataOutputStream(clientSocket.getOutputStream());
	    } catch (IOException ioe) {
		System.out
			.println("Problemi nella creazione degli stream di input/output "
				+ "su socket: ");
		ioe.printStackTrace();
		// il server continua l'esecuzione riprendendo dall'inizio del
		// ciclo
		return;
	    } catch (Exception e) {
		System.out
			.println("Problemi nella creazione degli stream di input/output "
				+ "su socket: ");
		e.printStackTrace();
		// il server continua l'esecuzione riprendendo dall'inizio del
		// ciclo
		return;
	    }
	    /* ricezione file */
	    String nomeFile;
	    // ricezione del nome
	    try {
		nomeFile = inSock.readUTF();
	    } catch (SocketTimeoutException ste) {
		System.out.println("Timeout scattato: ");
		ste.printStackTrace();
		clientSocket.close();
		System.out
			.print("\n^D(Unix)/^Z(Win)+invio per uscire, solo invio per continuare: ");
		// il client continua l'esecuzione riprendendo dall'inizio del
		// ciclo
		return;
	    } catch (Exception e) {
		System.out
			.println("Problemi nella ricezione del nome del file: ");
		e.printStackTrace();
		// servo nuove richieste
		return;
	    }
	    FileOutputStream outFile = null;
	    String esito;
	    if (nomeFile == null) {
		System.out
			.println("Problemi nella ricezione del nome del file: ");
		clientSocket.close();
		return;
	    } else {
		// controllo se il file esiste, se non esiste lo creo,
		// altrimenti
		// torno errore
		File curFile = new File(nomeFile);
		if (curFile.exists()) {
		    try {
			esito = "Sovrascritto file esistente";
			// distruggo il file da sovrascrivere
			curFile.delete();
		    } catch (Exception e) {
			System.out
				.println("Problemi nella notifica di file esistente: ");
			e.printStackTrace();
			// servo nuove richieste
			return;
		    }
		} else
		    esito = "Creato nuovo file";
		outFile = new FileOutputStream(nomeFile);
	    }
	    // ciclo di ricezione dal client, salvataggio file e stamapa a video
	    try {
		System.out.println("Ricevo il file " + nomeFile + ": \n");
		// FileUtility.trasferisci_a_byte_file_binario(inSock, new
		// DataOutputStream(outFile));
		System.out.println("\nRicezione del file " + nomeFile
			+ " terminata\n");
		// chiusura file
		outFile.close();
		// chiusura socket in downstream
		clientSocket.shutdownInput();
		// ritorno esito positivo al client
		outSock.writeUTF(esito + ", file salvato lato server");
		outSock.flush();
		// chiudo la socket anche in upstream
		clientSocket.shutdownOutput();
		System.out.println("\nTerminata connessione con "
			+ clientSocket);
	    } catch (SocketTimeoutException ste) {
		System.out.println("Timeout scattato: ");
		ste.printStackTrace();
		clientSocket.close();
		System.out
			.print("\n^D(Unix)/^Z(Win)+invio per uscire, solo invio per continuare: ");
		// il client continua l'esecuzione riprendendo dall'inizio del
		// ciclo
		return;
	    } catch (Exception e) {
		System.err
			.println("\nProblemi durante la ricezione e scrittura del file: "
				+ e.getMessage());
		e.printStackTrace();
		clientSocket.close();
		System.out.println("Terminata connessione con " + clientSocket);
		return;
		// il server continua a fornire il servizio ricominciando
		// dall'inizio del ciclo esterno
	    }
	}
	// qui catturo le eccezioni non catturate all'interno del while
	// in seguito alle quali il server termina l'esecuzione
	catch (Exception e) {
	    e.printStackTrace();
	    // chiusura di stream e socket
	    System.out
		    .println("Errore irreversibile, PutFileServerThread: termino...");
	    System.exit(3);
	}
    } // run

} // PutFileServerThread

