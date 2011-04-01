package nbody_distribuito.master;

import java.net.Socket;

import nbody.BodiesMap;

/**
 * @author Boccacci Andrea, Cicora Saverio
 * 
 */
public class ClientNetworkUtils {
    Socket socket;
    
    public ClientNetworkUtils(Socket socket){
	this.socket = socket;
    }
    
    public void computeSubMap(BodiesMap map) {
    }
    
    public void stopCompute(){}

}
