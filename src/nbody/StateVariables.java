package nbody;

import java.util.concurrent.ArrayBlockingQueue;

public class StateVariables {

	private float deltaTime;
	private Object dtLock;
	private float softFactor;
	private Object sfLock;
	private int numBodies;
	private Object nbLock;
	private ArrayBlockingQueue<BodiesMap> mapQueue;

	
	public StateVariables(){
		this.deltaTime = Float.parseFloat(System.getProperty("deltaTime", "0.5"));
		this.softFactor =  Float.parseFloat(System.getProperty("softFactor", "1.0"));
		this.numBodies = 0;
		this.dtLock = new Object();
		this.sfLock = new Object();
		this.nbLock = new Object();
		this.mapQueue = new ArrayBlockingQueue<BodiesMap>(50, true);
	}
	
	public void setDeltaTime(float value){
		synchronized(dtLock){
			this.deltaTime = value;
		}
	}
	public float getDeltaTime(){
		synchronized(dtLock){
			return this.deltaTime;
		}
	}
	public void setSoftFactor(float value){
		synchronized(sfLock){
			this.softFactor = value;
		}
	}
	public float getSoftFactor(){
		synchronized(sfLock){
			return this.softFactor;
		}
	}
	public void setNumBodies(int value){
		synchronized(nbLock){
			this.numBodies = value;
		}
	}
	public int getNumBodies(){
		synchronized(nbLock){
			return this.numBodies;
		}
	}
	public BodiesMap getMap() throws InterruptedException{
		return mapQueue.take();
	}
	public void putMap(BodiesMap map) throws InterruptedException{
		mapQueue.put(map);
	}
	public void clearPendingMaps() throws InterruptedException{
		mapQueue.clear();
	}
}