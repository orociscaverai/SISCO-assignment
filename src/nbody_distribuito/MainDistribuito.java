package nbody_distribuito;

import nbody_distribuito.client.Client;
import nbody_distribuito.master.Master;

public class MainDistribuito {

    public static void main(String args[]) {
	new Master("Master");
	
	new Client("Client-00", "127.0.0.1");
	
	try {
	    Thread.sleep(1000);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }

}
