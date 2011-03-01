package nbody_distribuito;

import java.io.IOException;

public class MainMaster {

    public static final int PORT = 54321;

    // porta al di fuori del range 1-1024!
    // dichiarata come statica perchè caratterizza il server

    public static void main(String[] args) throws IOException {

	// Porta sulla quale ascolta il server
	int port = -1;

	/* controllo argomenti */
	try {
	    if (args.length == 1) {
		port = Integer.parseInt(args[0]);
		// controllo che la porta sia nel range consentito 1024-65535
		if (port < 1024 || port > 65535) {
		    System.out
			    .println("Usage: java LineServer [serverPort>1024]");
		    System.exit(1);
		}
	    } else if (args.length == 0) {
		port = PORT;
	    } else {
		System.out
			.println("Usage: java PutFileServerThread or java PutFileServerThread port");
		System.exit(1);
	    }
	} // try
	catch (Exception e) {
	    System.out.println("Problemi, i seguenti: ");
	    e.printStackTrace();
	    System.out
		    .println("Usage: java PutFileServerThread or java PutFileServerThread port");
	    System.exit(1);
	}
    }
}
