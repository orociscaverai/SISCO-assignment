package nbody_distribuito.master;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import pcd.actors.Actor;
import pcd.actors.Message;
import pcd.actors.Port;

public class Master extends Actor {

	private Vector<Port> workers;

	public Master(String actorName) {
		super(actorName);
		workers = new Vector<Port>();
	}

	private void compute() {
		PartitionStrategy ps = new MyStrategy();
		Tree tr = ps.partitionMap();
	}

	@Override
	public void run() {
		try {
			while(true){
				log("wait");
				Message res = receive();
				log("received item "+res.toString());
				if (res.getType().equalsIgnoreCase("associate")){
					log(""+res.getArg(0));
					Port p = (Port)res.getArg(0);
					workers.add(p);
					log("porta = "+p.toString());
					send(p,new Message("ack_associate"));
					log("message sent");
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void log(String string) {
		System.out.println("Master : "+string);
		
	}
}
