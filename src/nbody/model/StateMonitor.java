package nbody.model;

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
    private static final int START = 0;
    private static final int PAUSE = 1;
    private static final int STOP = 2;

    public final static int NOTHING = -1;
    public final static int COMPUTE = 0;
    public final static int RANDOMIZE = 1;
    public final static int STEP = 2;
    private int actionType;

    private ReentrantReadWriteLock lock;
    private Lock r, w;
    private Condition canCompute;

    public StateMonitor() {
	lock = new ReentrantReadWriteLock();
	r = lock.readLock();
	w = lock.writeLock();
	canCompute = w.newCondition();

	this.runState = STOP;
	this.actionType = NOTHING;

    }

    /**
     * Da il via alla computazione segnalando la cosa ad eventuali thred in
     * attesa
     */
    public void startProcess() {
	w.lock();
	try {
	    // Azione valida solo se non sono in stato di START, negli altri
	    // caso ignoro il comando
	    if (runState > START) {
		runState = START;
		actionType = COMPUTE;
		canCompute.signal();
	    } else {
		log("StateMonitor: è stata richiesto lo START, ma lo stato dell'applicazione non è Pause o Stop");
	    }
	} finally {
	    w.unlock();
	}
    }

    /**
     * Da il via ad una singola computazione segnalando la cosa ad eventuali
     * thred in attesa.
     */
    public void stepProcess() {
	w.lock();
	try {
	    if (runState == PAUSE) {
		actionType = STEP;
		canCompute.signal();
	    } else {
		log("StateMonitor: è stato richiesto lo Step, ma lo stato dell'applicazione non è PAUSE");
	    }
	} finally {
	    w.unlock();
	}

    }

    /**
     * Segnala la volontà di inizializzare la mappa con valori random. Comando
     * valido sole se la computazione è ferma
     */
    public void notifyRandomize() {
	w.lock();
	try {
	    if (runState == STOP) {
		actionType = RANDOMIZE;
		canCompute.signal();
	    } else {
		log("StateMonitor: è stato richiesto la Randomize, ma lo stato dell'applicazione non è STOP");
	    }
	} finally {
	    w.unlock();
	}
    }

    /**
     * Imposta lo stato del monitor in PAUSE. Questa condizione metterà in
     * attesa i thread che chiamano il metodo waitAction(), sino a quando non
     * viene inviato un comando di start, o step.
     */
    public void pauseProcess() {
	w.lock();
	try {
	    if (runState == START) {
		runState = PAUSE;
		actionType = NOTHING;
	    } else {
		log("StateMonitor: è stata richiesta la Pause, ma lo stato dell'applicazione non è Runing");
	    }
	} finally {
	    w.unlock();
	}
    }

    /**
     * Permette di richiedere lo Stop dell'applicazione. Lo Stop non avviene in
     * maniera immediata, ma verrà settato un flag interno. Il thread adibito ad
     * eseguire la computazione interrogherà il metodo isStopped() per conoscere
     * lo stato del flag
     */
    public void stopProcess() {
	w.lock();
	try {
	    if (runState < STOP) {
		runState = STOP;
		actionType = NOTHING;
	    } else {
		log("StateMonitor: è stato richiesto lo Stop, ma lo stato dell'applicazione non è Runing");
	    }
	} finally {
	    w.unlock();
	}
    }

    /**
     * È un metodo bloccante che, nel caso lo stato è Stopped o Paused, blocca
     * il Thread chiamante. Lo sblocco avviene invocando un comando di start,
     * randomize e step.
     */
    public int waitAction() throws InterruptedException {
	w.lock();
	try {
	    while (runState > START && actionType == NOTHING) {
		canCompute.await();
	    }
	    int n = actionType;
	    if (actionType != COMPUTE) {
		actionType = NOTHING;
	    }
	    return n;
	} finally {
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
	synchronized (System.out) {
	    System.out.println(message);
	}
    }

}