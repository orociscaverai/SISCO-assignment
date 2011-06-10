package nbody_distribuito.controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import nbody_distribuito.event.Event;
import pcd.actors.Actor;

/**
 * Questa classe funge da adattatore tra la gestione ad eventi e la estione a
 * scambio di messaggi actor like
 * 
 * */
public abstract class ControllerAgent extends Actor implements ObserverInterface {

    private BlockingQueue<Event> queue;

    protected ControllerAgent(String actorName) {
	super(actorName);
	queue = new ArrayBlockingQueue<Event>(100);
    }

    protected boolean isEventAvailable() throws InterruptedException {
	return !queue.isEmpty();
    }

    protected Event fetchEvent() throws InterruptedException {
	return queue.take();
    }

    protected Event fetchEventIfPresent() throws InterruptedException {
	return queue.poll();
    }

    public void notifyEvent(Event ev) {
	queue.add(ev);
    }

    protected void log(String msg) {
	System.out.println(getActorName() + ": " + msg);
    }
}
