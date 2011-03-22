package nbody;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Questo monitor serve a gestire lo stato dell'applicazione per garantire la
 * coerenza d'esecuzione
 * 
 * Gli stati possibili possono essere: Runned, Paused, Stopped. In più è
 * possibile eseguire un singolo step.
 * 
 * Se lo stato è Runned è possibile passare in Paused o Stopped Se lo stato è
 * Paused è possibile passare in Stopped, Runned oppure richiedere l'esecuzione
 * di un singolo step. Se lo stato è Stopped è possibile passare in Started.
 * 
 * */
public class StateMonitor {

    private int runState;
    static final int START = 0;
    static final int PAUSE = 1;
    static final int STOP = 2;
    static final int STOPPED = 3;

    private boolean singleStep;

    private ReentrantReadWriteLock lock;
    private Lock r,w;
    private Condition isStarted,isStopped;

    public StateMonitor() {
	lock = new ReentrantReadWriteLock();
	r = lock.readLock();
	w = lock.writeLock();
	isStarted = w.newCondition();
	isStopped = w.newCondition();

	this.runState = STOP;
	this.singleStep = false;
    }

    public void startProcess() {
	w.lock();
	try {
	    if (runState > START) {
		runState = START;
		isStarted.signalAll();
	    } else {
		log("StateMonitor: è stata richiesto lo START, ma lo stato dell'applicazione non è Pause o Stop");
	    }
	} finally {
	    w.unlock();
	}
    }


    public void stepProcess() {
	w.lock();
	try {
	    if (runState == PAUSE) {
		singleStep = true;
		isStarted.signalAll();
	    } else {
		log("StateMonitor: è stato richiesto lo Step, ma lo stato dell'applicazione non è PAUSE");
	    }
	} finally {
	    w.unlock();
	}

    }

    public void pauseProcess() {
	w.lock();
	try {
	    if (runState == START) {
		runState = PAUSE;
	    } else {
		log("StateMonitor: è stata richiesta la Pause, ma lo stato dell'applicazione non è Runing");
	    }
	} finally {
	    w.unlock();
	}
    }

    /**
     * Permette di richiedere lo Stop dell'applicazione. Lo Stop non avviene in
     * maniera immediata, ma verrà settato un flag interno che servirà a
     * notificare all'intera applicazione di interrompere la computazione
     */
    public void stopProcess() {
	w.lock();
	try {
	    if (runState < STOP) {
		runState = STOP;
		singleStep = false;
	    } else {
		log("StateMonitor: è stato richiesto lo Stop, ma lo stato dell'applicazione non è Runing");
	    }
	} finally {
	    w.unlock();
	}
    }

    /**
     * È un metodo bloccante che, nel caso lo stato è Stopped o Paused, blocca
     * il Thread chiamante sino a quando non viene richiesto lo Start o lo Step
     */
    public void waitStart() throws InterruptedException {
    	
    r.lock();
    try{
    	if (runState == START || singleStep){
    		return;
    	}
    }finally{
    	r.unlock();
    }
	w.lock();
	try {
	    // è necessario il while dato che potrebbero verificarsi degli
	    // "spurious wakeup" come indicato nella documentazione Java
	    while ((runState > START) && !singleStep) {
		isStarted.await();
	    }
	    singleStep = false;
	} finally {
	    w.unlock();
	}
    }
    
    public void waitStopped() throws InterruptedException{
    	r.lock();
    	try{
    		if (runState == STOPPED)
    			return;
    	}finally{
    		r.unlock();
    	}
    	w.lock();
    	try{
    		while(runState!= STOPPED)
    			isStopped.await();
    	}finally{
    		w.unlock();
    	}
    }
    public boolean isStopped() {
	r.lock();
	try {
	    if (runState == STOP) {
		return true;
	    } else {
		return false;
	    }
	} finally {
	    r.unlock();
	}
    }

    private void log(String message) {
	System.out.println(message);
    }

}


//
//
//
//package nbody;
//
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//import java.util.concurrent.locks.ReentrantReadWriteLock;
//
///**
// * Questo monitor serve a gestire lo stato dell'applicazione per garantire la
// * coerenza d'esecuzione
// * 
// * Gli stati possibili possono essere: Runned, Paused, Stopped. In più è
// * possibile eseguire un singolo step.
// * 
// * Se lo stato è Runned è possibile passare in Paused o Stopped Se lo stato è
// * Paused è possibile passare in Stopped, Runned oppure richiedere l'esecuzione
// * di un singolo step. Se lo stato è Stopped è possibile passare in Started.
// * 
// * */
//public class StateMonitor {
//
//	private boolean isRunning;
//	private boolean isPaused;
//	private boolean singleStep;
//	private ReentrantReadWriteLock rwl;
//	private Lock lock;
//	private Condition waitRandomize,waitStart;
//
//	public StateMonitor() {
//		this.isRunning = false;
//		this.isPaused = false;
//		this.singleStep = false;
//		lock = new ReentrantLock();
//		rwl = new ReentrantReadWriteLock();
//		waitRandomize = lock.newCondition();
//		waitStart = rwl.writeLock().newCondition();
//	}
//
//	public void startProcess() {
//		rwl.writeLock().lock();
//		try{
//			if (!isRunning || isPaused) {
//				isPaused = false;
//				isRunning = true;
//				singleStep = false;
//				waitStart.signalAll();
//			} else {
//				log("StateMonitor: è stata richiesto lo Start, ma lo stato dell'applicazione non è Paused o Stopped");
//			}
//		}finally{
//			rwl.writeLock().unlock();
//		}
//	}
//
//	public void pauseProcess() {
//		rwl.writeLock().lock();
//		try{
//			if (isRunning) {
//				isPaused = true;
//				isRunning = false;
//			} else {
//				log("StateMonitor: è stata richiesta la Pause, ma lo stato dell'applicazione non è Runing");
//			}
//		}finally{
//			rwl.writeLock().unlock();
//		}
//	}
//
//	public void step() {
//		rwl.writeLock().lock();
//		try{
//			if (!isRunning) {
//				singleStep = true;
//				waitStart.signalAll();
//			} else {
//				log("StateMonitor: è stato richiesto lo Step, ma lo stato dell'applicazione non è Runing");
//			}
//		}finally{
//			rwl.writeLock().unlock();
//		}
//
//	}
//
//	/**
//	 * Permette di richiedere lo Stop dell'applicazione. Lo Stop non avviene in
//	 * maniera immediata, ma verrà settato un flag interno che servirà a
//	 * notificare all'intera applicazione di interrompere la computazione
//	 */
//	public void stopProcess() {
//		rwl.writeLock().lock();
//		try{
//			if (isRunning || isPaused) {
//				isPaused = false;
//				isRunning = false;
//				singleStep = false;
//			} else {
//				log("StateMonitor: è stato richiesto lo Stop, ma lo stato dell'applicazione non è Runing");
//			}
//		}finally{
//			rwl.writeLock().unlock();
//		}
//	}
//
//	/**
//	 * È un metodo bloccante che, nel caso lo stato è Stopped o Paused, blocca
//	 * il Thread chiamante sino a quando non viene richiesto lo Start o lo Step
//	 */
//	public void waitStart() throws InterruptedException {
//		rwl.readLock().lock();
//		try{
//			//log("Attendo? " + ((!isRunning || isPaused) && !singleStep));
//			//TODO mi sembra che in questo caso ci vada l'if... non sono sicuro però
//			if ((isRunning && !isPaused)/* || singleStep*/) {
//				return;
//			}
//		}finally{
//			rwl.readLock().unlock();
//		}
//		rwl.writeLock().lock();
//		try{
//			log("Attendo? " + ((!isRunning || isPaused)/* && !singleStep*/));
//			//TODO mi sembra che in questo caso ci vada l'if... non sono sicuro però
//			if ((!isRunning || isPaused)/* && !singleStep*/) {
//				waitStart.await();
//			}
//		}finally{
//			rwl.writeLock().unlock();
//		}
//	}
//
//	public boolean isStopped() {
//		rwl.readLock().lock();
//		try{
//			if (!isRunning && !isPaused) {
//				return true;
//			} else {
//				return false;
//			}
//		}finally{
//			rwl.readLock().unlock();
//		}
//	}
//	public boolean isSuspended() {
//		rwl.readLock().lock();
//		try{
//			if (!isRunning || isPaused) {
//				return true;
//			} else {
//				return false;
//			}
//		}finally{
//			rwl.readLock().unlock();
//		}
//	}
//
//	private void log(String message) {
//		System.out.println(message);
//	}
//	public void waitRandomize() throws InterruptedException{
//		lock.lock();
//		try{
//			waitRandomize.await();
//		}finally{
//			lock.unlock();
//		}
//	}
//	public void notifyRandomize(){
//		lock.lock();
//		try{
//			waitRandomize.signalAll();
//		}finally{
//			lock.unlock();
//		}
//	}
//
//}
