package nbody;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import nbody.event.Event;
import controller.ObserverInterface;

public class ControllerAgent extends Thread implements ObserverInterface {

    private BlockingQueue<Event> queue;

    public ControllerAgent(String name) {
	super(name);
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

    @Override
    public void notifyEvent(Event ev) {
	queue.add(ev);
    }

    protected void log(String msg) {
	synchronized (System.out) {
	    System.out.println("[ " + getName() + " ] " + msg);
	}
    }
}