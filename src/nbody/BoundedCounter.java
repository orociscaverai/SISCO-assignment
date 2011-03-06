package nbody;

public class BoundedCounter {

	private int max;
	private int count;
	
	public BoundedCounter(int max){
		this.max = max;
		count = 0;
	}
	
	public synchronized void reset(){
		count = 0;
	}
	
	public synchronized void inc(){
		count++;
		if (count >= max){
			notifyAll();
		}
	}
	
	public synchronized void awaitForMax() throws InterruptedException {
		while (count < max){
			wait();
		}
	}
}
