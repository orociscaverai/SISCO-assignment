package nbody_distribuito.testCase;

import nbody_distribuito.master.strategy.SplitStrategyUsingNewJob;
import nbody_distribuito.model.BodiesMap;
import nbody_distribuito.shared_object.Job;

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
