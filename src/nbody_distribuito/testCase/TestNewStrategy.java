package nbody_distribuito.testCase;

import nbody_distribuito.BodiesMap;
import nbody_distribuito.master.Job;
import nbody_distribuito.master.SplitStrategyUsingNewJob;

public class TestNewStrategy {
	public static void main(String[] args){
		SplitStrategyUsingNewJob ps = new SplitStrategyUsingNewJob();
		BodiesMap bm = new BodiesMap();
		bm.generateRandomMap(10);
		ps.splitJob(3, bm);
		Job j = ps.getNextJob();
		j = ps.getNextJob();
		j = ps.getNextJob();
		j = ps.getNextJob();
		j = ps.getNextJob();
		System.out.println(j.toString());
	}
}
