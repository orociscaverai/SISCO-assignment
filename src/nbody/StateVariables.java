package nbody;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StateVariables {

	private float deltaTime;
	private ReentrantReadWriteLock dtLock;
	private float softFactor;
	private ReentrantReadWriteLock sfLock;
	private int numBodies;
	private ReentrantReadWriteLock nbLock;

	
	public StateVariables(){
		this.deltaTime = Float.parseFloat(System.getProperty("deltaTime", "0.5"));
		this.softFactor =  Float.parseFloat(System.getProperty("softFactor", "1.0"));
		this.numBodies = 0;
		this.dtLock = new ReentrantReadWriteLock();
		this.sfLock = new ReentrantReadWriteLock();
		this.nbLock = new ReentrantReadWriteLock();
	}
	
	public void setDeltaTime(float value){
		dtLock.writeLock().lock();
		try{
			this.deltaTime = value;
		}finally{
			dtLock.writeLock().unlock();
		}
	}
	public float getDeltaTime(){
		dtLock.readLock().lock();
		try{
			return this.deltaTime;
		}finally{
			dtLock.readLock().unlock();
		}
	}
	public void setSoftFactor(float value){
		sfLock.writeLock().lock();
		try{
			this.softFactor = value;
		}finally{
			sfLock.writeLock().unlock();
		}
	}
	public float getSoftFactor(){
		sfLock.readLock().lock();
		try{
			return this.softFactor;
		}finally{
			sfLock.readLock().unlock();
		}
	}
	public void setNumBodies(int value){
		nbLock.writeLock().lock();
		try{
			this.numBodies = value;
		}finally{
			nbLock.writeLock().unlock();
		}
	}
	public int getNumBodies(){
		nbLock.readLock().lock();
		try{
			return this.numBodies;
		}finally{
			nbLock.readLock().unlock();
		}
	}
}