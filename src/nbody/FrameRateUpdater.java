package nbody;

import gui.NBodySetListener;

import java.util.ArrayList;


public class FrameRateUpdater extends Thread{

	private ArrayList<NBodySetListener> listeners;
	private StateVariables var;
	private long timestep = 30;

	public FrameRateUpdater(StateVariables var) {
		this.var = var;
		listeners = new ArrayList<NBodySetListener>();
	}


	public void run(){
		long nextComputeTime;
		nextComputeTime = System.currentTimeMillis();
		while(true){
			try{
				long ready = nextComputeTime - System.currentTimeMillis();
				
				if(ready>0){
					log("sleep for "+ready+" ms");
					Thread.sleep(ready);
				}
				notifyListeners();
				nextComputeTime += timestep;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public void addListener(NBodySetListener l){
		listeners.add(l);
	}

	private void notifyListeners() throws InterruptedException{
		for (NBodySetListener l: listeners){
			l.setUpdated(var.getMap());
		}
	}	
	private void log(String log){
		System.out.println("[FPS Updater] "+log);
	}
}
