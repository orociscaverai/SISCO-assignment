package nbody_distribuito.message;

import nbody_distribuito.Constants;
import nbody_distribuito.master.Job;
import pcd.actors.Message;

public class DoJobMessage extends Message {

    private static final long serialVersionUID = 1L;

    private static final String messageType = Constants.DO_JOB;

    private final int workerID;
    private final Job job;

    private final float deltaTime, softFactor;

    public DoJobMessage(int workerID, Job job, float deltaTime, float softFactor) {
	super(messageType);

	this.workerID = workerID;
	this.job = job;
	this.deltaTime = deltaTime;
	this.softFactor = softFactor;
    }

    /**
     * @return the workerID
     */
    public int getWorkerID() {
	return workerID;
    }

    /**
     * @return the job
     */
    public Job getJob() {
	return job;
    }

    /**
     * @return the deltaTime
     */
    public float getDeltaTime() {
	return deltaTime;
    }

    /**
     * @return the softFactor
     */
    public float getSoftFactor() {
	return softFactor;
    }

    @Override
    public String toString() {

	String args = getType() + " ";

	args += "workerID: ";
	args += workerID;
	args += "Job: ";
	args += job.toString();
	args += "deltaTime: ";
	args += deltaTime;
	args += "softFactor: ";
	args += softFactor;

	return args;

    }

}
