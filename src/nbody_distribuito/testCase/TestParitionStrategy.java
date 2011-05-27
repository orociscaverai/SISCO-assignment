package nbody_distribuito.testCase;

import nbody_distribuito.BodiesMap;
import nbody_distribuito.master.SimpleSplitStrategy;

public class TestParitionStrategy {

    /**
     * @param args
     */
    public static void main(String[] args) {

	
	SimpleSplitStrategy ps = new SimpleSplitStrategy();
	
	BodiesMap bm = new BodiesMap();
	bm.generateRandomMap(100);
	ps.splitJob(3, bm);
	bm.generateRandomMap(10);
	ps.splitJob(2, bm);
	bm.generateRandomMap(11);
	ps.splitJob(2, bm);
	
	
	
    }

}
