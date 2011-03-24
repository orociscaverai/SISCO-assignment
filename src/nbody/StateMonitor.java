package nbody;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Questo monitor serve a gestire lo stato dell'applicazione per garantire la
 * coerenza d'esecuzione
 * 
 * Gli stati possibili possono essere: Runned, Paused, Stopped. In pi� �
 * possibile eseguire un singolo step.
 * 
 * Se lo stato � Runned � possibile passare in Paused o Stopped Se lo stato �
 * Paused � possibile passare in Stopped, Runned oppure richiedere l'esecuzione
 * di un singolo step. Se lo stato � Stopped � possibile passare in Started.
 * 
 * */
public class StateMonitor {

    private int runState;
    static final int START = 0;
    static final int PAUSE = 1;
    static final int STOP = 2;
    static final int STOPPED = 3;

    private boolean singleStep;
    private boolean randomize;
    private boolean updateSignaled,stepSignaled;
    
    private ReentrantReadWriteLock lock;
    private Lock r, w;
    private Condition isStarted, isStopped, canUpdate,canCompute;
	

    public StateMonitor() {
	lock = new ReentrantReadWriteLock();
	r = lock.readLock();
	w = lock.writeLock();
	isStarted = w.newCondition();
	isStopped = w.newCondition();
	canUpdate = w.newCondition();
	canCompute = w.newCondition();

	this.runState = STOP;
	this.singleStep = false;
	this.randomize = false;
	this.updateSignaled = false;
	this.stepSignaled = false;
    }

    public void startProcess() {
	w.lock();
	try {
	    if (runState > START) {
		runState = START;
		isStarted.signalAll();
		canCompute.signal();
		canUpdate.signal();
	    } else {
		log("StateMonitor: � stata richiesto lo START, ma lo stato dell'applicazione non � Pause o Stop");
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
		updateSignaled = true;
		canUpdate.signal();
		isStarted.signalAll();
	    } else {
		log("StateMonitor: � stato richiesto lo Step, ma lo stato dell'applicazione non � PAUSE");
	    }
	} finally {
	    w.unlock();
	}

    }
    public void notifyRandomize(){
    	w.lock();
    	try{
    		randomize = true;
    		updateSignaled = true;
    		canCompute.signal();
    		canUpdate.signal();
    	}finally{
    		w.unlock();
    	}
    }

    public void pauseProcess() {
	w.lock();
	try {
	    if (runState == START) {
		runState = PAUSE;
	    } else {
		log("StateMonitor: � stata richiesta la Pause, ma lo stato dell'applicazione non � Runing");
	    }
	} finally {
	    w.unlock();
	}
    }

    /**
     * Permette di richiedere lo Stop dell'applicazione. Lo Stop non avviene in
     * maniera immediata, ma verr� settato un flag interno che servir� a
     * notificare all'intera applicazione di interrompere la computazione
     */
    public void stopProcess() {
	w.lock();
	try {
	    if (runState < STOP) {
		runState = STOP;
		singleStep = false;
		updateSignaled = true;
		canUpdate.signal();
	    } else {
		log("StateMonitor: � stato richiesto lo Stop, ma lo stato dell'applicazione non � Runing");
	    }
	} finally {
	    w.unlock();
	}
    }

    /**
     * � un metodo bloccante che, nel caso lo stato � Stopped o Paused, blocca
     * il Thread chiamante sino a quando non viene richiesto lo Start o lo Step
     */
    public void waitStart() throws InterruptedException {

	r.lock();
	try {
	    if (runState == START || singleStep) {
		return;
	    }
	} finally {
	    r.unlock();
	}
	w.lock();
	try {
	    // � necessario il while dato che potrebbero verificarsi degli
	    // "spurious wakeup" come indicato nella documentazione Java
	    while ((runState > START) && !singleStep) {
		isStarted.await();
	    }
	    singleStep = false;
	} finally {
	    w.unlock();
	}
    }

    public void waitStopped() throws InterruptedException {
	r.lock();
	try {
	    if (runState == STOPPED)
		return;
	} finally {
	    r.unlock();
	}
	w.lock();
	try {
	    while (runState != STOPPED)
		isStopped.await();
	} finally {
	    w.unlock();
	}
    }
    public void WaitUpdate() throws InterruptedException{
    	r.lock();
    	try{
    		if(runState == START)
    			return;
    	}finally{
    		r.unlock();
    	}
    	w.lock();
    	try{
    		while(runState != START && !updateSignaled){
    			canUpdate.await();
    		}
    		updateSignaled = false;
    	}finally{
    		w.unlock();
    	}
    }
    public void waitAction() throws InterruptedException{
    	r.lock();
    	try{
    		while(runState<STOP)
    			return;
    	}finally{
    		r.unlock();
    	}
    	w.lock();
    	try{
    		while(runState >= STOP && !randomize)
    			canCompute.await();
    		randomize = false;
    	}finally{
    		w.unlock();
    	}
    }
    public boolean isSuspended(){
    	r.lock();
    	try{
    		return runState>START;
    	}finally{
    		r.unlock();
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

    public boolean isStarted() {
	return true;
    }

    private void log(String message) {
	System.out.println(message);
    }

}