package nbody_distribuito.master;

import nbody_distribuito.MyStrategy;

public class Master {

    private void compute() {
	PartitionStrategy ps = new MyStrategy();
	Tree tr = ps.partitionMap();
    }

}
