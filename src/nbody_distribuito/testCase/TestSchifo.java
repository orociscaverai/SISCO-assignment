package nbody_distribuito.testCase;

import pcd.actors.Port;
import nbody_distribuito.Body;
import nbody_distribuito.master.Job;
import nbody_distribuito.master.JobResult;
import nbody_distribuito.master.WorkerHandlerActor;
import nbody_distribuito.master.filter.QueueFilter;
import nbody_distribuito.worker.Worker;

public class TestSchifo {

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub
//	Body b = new Body(1, 1.0f);
//	float[] pos = new float[2];
//	pos[0] = 0;
//	pos[1] = 0;
//	b.setPosition(pos);
//	float[] pos2 = b.getPosition();
//	pos2[0] += 1;
//	System.out.println(b.toString());
Worker w = new Worker("pippo","pluto","topolino",new QueueFilter());
try {
	Job j = new Job();
	float[] pos = new float[2];
	j.addDataOnFirstSet(1, pos, 1.0f);
	JobResult jr = w.doCompute(j, 1.0f, 1f);
	System.out.println(jr.getResultList().get(0).getBodyId());
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

    }

}
