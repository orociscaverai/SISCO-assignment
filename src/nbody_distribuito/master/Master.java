package nbody_distribuito.master;

import pcd.actors.Actor;
import nbody_distribuito.MyStrategy;

public class Master extends Actor {

    public Master(String actorName) {
	super(actorName);
    }

    private void compute() {
	PartitionStrategy ps = new MyStrategy();
	Tree tr = ps.partitionMap();
    }

    @Override
    public void run() {
	// TODO Auto-generated method stub

    }

}
