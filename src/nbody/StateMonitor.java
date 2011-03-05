package nbody;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

	private boolean isRunning;
	private boolean isPaused;
	private boolean singleStep;
	private Lock lock;
	private Condition waitRandomize;

	public StateMonitor() {
		this.isRunning = false;
		this.isPaused = false;
		this.singleStep = false;
		lock = new ReentrantLock();
		waitRandomize = lock.newCondition();
	}

	public synchronized void startProcess() {

		if (!isRunning || isPaused) {
			isPaused = false;
			isRunning = true;
			notifyAll();
		} else {
			log("StateMonitor: è stata richiesto lo Start, ma lo stato dell'applicazione non è Paused o Stopped");
		}
	}

	public synchronized void pauseProcess() {
		if (isRunning) {
			isPaused = true;
			isRunning = false;
		} else {
			log("StateMonitor: è stata richiesta la Pause, ma lo stato dell'applicazione non è Runing");
		}
	}

	public synchronized void step() {
		if (!isRunning) {
			singleStep = true;
			notifyAll();
		} else {
			log("StateMonitor: è stato richiesto lo Step, ma lo stato dell'applicazione non è Runing");
		}

	}

	/**
	 * Permette di richiedere lo Stop dell'applicazione. Lo Stop non avviene in
	 * maniera immediata, ma verrà settato un flag interno che servirà a
	 * notificare all'intera applicazione di interrompere la computazione
	 */
	public synchronized void stopProcess() {
		if (isRunning || isPaused) {
			isPaused = false;
			isRunning = false;
			singleStep = false;
		} else {
			log("StateMonitor: è stato richiesto lo Stop, ma lo stato dell'applicazione non è Runing");
		}
	}

	/**
	 * È un metodo bloccante che, nel caso lo stato è Stopped o Paused, blocca
	 * il Thread chiamante sino a quando non viene richiesto lo Start o lo Step
	 */
	public synchronized void waitStart() throws InterruptedException {
		//log("Attendo? " + ((!isRunning || isPaused) && !singleStep));
		while ((!isRunning || isPaused) && !singleStep) {
			wait();
		}
		singleStep = false;
	}

	public synchronized boolean isStopped() {
		if (!isRunning && !isPaused) {
			return true;
		} else {
			return false;
		}
	}
	public synchronized boolean isSuspended() {
		if (!isRunning || isPaused) {
			return true;
		} else {
			return false;
		}
	}

	private void log(String message) {
		System.out.println(message);
	}
	public void waitRandomize() throws InterruptedException{
		lock.lock();
		try{
			waitRandomize.await();
		}finally{
			lock.unlock();
		}
	}
	public void notifyRandomize(){
		lock.lock();
		try{
			waitRandomize.signalAll();
		}finally{
			lock.unlock();
		}
	}

}
