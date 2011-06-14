package nbody_distribuito.testCase;

import nbody_distribuito.master.strategy.PartitionStrategy;
import nbody_distribuito.master.strategy.StrategyFactory;
import nbody_distribuito.model.BodiesMap;

public class TestParitionStrategy {

    /**
     * @param args
     */
    public static void main(String[] args) {

    PartitionStrategy ps = new StrategyFactory().getStrategy();
	BodiesMap bm = new BodiesMap();
	bm.generateRandomMap(100);
    ps.splitJob(2, bm);
	
//	SimpleSplitStrategy ps = new SimpleSplitStrategy();
//	
//	BodiesMap bm = new BodiesMap();
//	bm.generateRandomMap(100);
//	ps.splitJob(3, bm);
//	Job j = ps.getNextJob();
//	List<ClientData> data = j.getFistSet();
//	for(int i = 0 ; i< data.size(); i++){
//		ClientData cl = data.get(i);
//		System.out.println("client data contains index "+cl.getId());
//	}
//	System.out.println("end of data");
//	data = j.getSecondSet();
//	for(int i = 0 ; i< data.size(); i++){
//		ClientData cl = data.get(i);
//		System.out.println("client data contains index "+cl.getId());
//	}
//	System.out.println("end of data");
//	j = ps.getNextJob();
//	data = j.getFistSet();
//	for(int i = 0 ; i< data.size(); i++){
//		ClientData cl = data.get(i);
//		System.out.println("client data contains index "+cl.getId());
//	}
//	System.out.println("end of data");
//	data = j.getSecondSet();
//	for(int i = 0 ; i< data.size(); i++){
//		ClientData cl = data.get(i);
//		System.out.println("client data contains index "+cl.getId());
//	}
//	j = ps.getNextJob();
//	j = ps.getNextJob();
//	j = ps.getNextJob();
//	j = ps.getNextJob();
//	j = ps.getNextJob();
//	if (j == null){
//		System.out.println("true");
//	}
//	bm.generateRandomMap(10);
//	ps.splitJob(2, bm);
//	bm.generateRandomMap(11);
//	ps.splitJob(2, bm);
	
	
	
    }

}
