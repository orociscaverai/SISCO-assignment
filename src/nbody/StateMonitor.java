package nbody;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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

	private boolean isRunning;
	private boolean isPaused;
	private boolean singleStep;
	private ReentrantReadWriteLock rwl;
	private Lock lock;
	private Condition waitRandomize,waitStart;

	public StateMonitor() {
		this.isRunning = false;
		this.isPaused = false;
		this.singleStep = false;
		lock = new ReentrantLock();
		rwl = new ReentrantReadWriteLock();
		waitRandomize = lock.newCondition();
		waitStart = rwl.writeLock().newCondition();
	}

	public void startProcess() {
		rwl.writeLock().lock();
		try{
			if (!isRunning || isPaused) {
				isPaused = false;
				isRunning = true;
				singleStep = false;
				waitStart.signalAll();
			} else {
				log("StateMonitor: � stata richiesto lo Start, ma lo stato dell'applicazione non � Paused o Stopped");
			}
		}finally{
			rwl.writeLock().unlock();
		}
	}

	public void pauseProcess() {
		rwl.writeLock().lock();
		try{
			if (isRunning) {
				isPaused = true;
				isRunning = false;
			} else {
				log("StateMonitor: � stata richiesta la Pause, ma lo stato dell'applicazione non � Runing");
			}
		}finally{
			rwl.writeLock().unlock();
		}
	}

	public void step() {
		rwl.writeLock().lock();
		try{
			if (!isRunning) {
				singleStep = true;
				waitStart.signalAll();
			} else {
				log("StateMonitor: � stato richiesto lo Step, ma lo stato dell'applicazione non � Runing");
			}
		}finally{
			rwl.writeLock().unlock();
		}

	}

	/**
	 * Permette di richiedere lo Stop dell'applicazione. Lo Stop non avviene in
	 * maniera immediata, ma verr� settato un flag interno che servir� a
	 * notificare all'intera applicazione di interrompere la computazione
	 */
	public void stopProcess() {
		rwl.writeLock().lock();
		try{
			if (isRunning || isPaused) {
				isPaused = false;
				isRunning = false;
				singleStep = false;
			} else {
				log("StateMonitor: � stato richiesto lo Stop, ma lo stato dell'applicazione non � Runing");
			}
		}finally{
			rwl.writeLock().unlock();
		}
	}

	/**
	 * � un metodo bloccante che, nel caso lo stato � Stopped o Paused, blocca
	 * il Thread chiamante sino a quando non viene richiesto lo Start o lo Step
	 */
	public void waitStart() throws InterruptedException {
		rwl.readLock().lock();
		try{
			//log("Attendo? " + ((!isRunning || isPaused) && !singleStep));
			//TODO mi sembra che in questo caso ci vada l'if... non sono sicuro per�
			if ((isRunning && !isPaused) || singleStep) {
				return;
			}
		}finally{
			rwl.readLock().unlock();
		}
		rwl.writeLock().lock();
		try{
			log("Attendo? " + ((!isRunning || isPaused) && !singleStep));
			//TODO mi sembra che in questo caso ci vada l'if... non sono sicuro per�
			if ((!isRunning || isPaused) && !singleStep) {
				waitStart.await();
			}
		}finally{
			rwl.writeLock().unlock();
		}
	}

	public boolean isStopped() {
		rwl.readLock().lock();
		try{
			if (!isRunning && !isPaused) {
				return true;
			} else {
				return false;
			}
		}finally{
			rwl.readLock().unlock();
		}
	}
	public boolean isSuspended() {
		rwl.readLock().lock();
		try{
			if (!isRunning || isPaused) {
				return true;
			} else {
				return false;
			}
		}finally{
			rwl.readLock().unlock();
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