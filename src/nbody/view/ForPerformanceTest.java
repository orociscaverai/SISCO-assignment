package nbody.view;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import nbody.BodiesMap;
import nbody.event.ChangeParamEvent;
import nbody.event.Event;
import nbody.event.PausedEvent;
import nbody.event.RandomizeEvent;
import nbody.event.SingleStepEvent;
import nbody.event.StartedEvent;
import nbody.event.StoppedEvent;
import nbody.view.swing.AbstractView;

public class ForPerformanceTest extends AbstractView implements Runnable {
    CountDownLatch counter;

    public void setUpdated(BodiesMap map) {
	if (counter != null) {
	    counter.countDown();
	}
    }

    public void setProcessingState() {
    }

    public void setCompletedState(final long dt) {
    }

    public void setInterruptedState(final long dt) {
    }

    public void setParameter(final float deltaTime, final float softFactor) {
    }

    private void notifyParameterChanged() {
	float deltaTime = 0.5f;
	float softFactor = 0.5f;
	Event ev = new ChangeParamEvent(null, deltaTime, softFactor);
	notifyEvent(ev);
    }

    private void notifyStarted() {
	Event ev = new StartedEvent(null);
	notifyEvent(ev);
    }

    private void notifyStopped() {
	Event ev = new StoppedEvent(null);
	notifyEvent(ev);
    }

    private void notifyPaused() {
	Event ev = new PausedEvent(null);
	notifyEvent(ev);
    }

    private void notifySingleStep() {
	Event ev = new SingleStepEvent(null);
	notifyEvent(ev);
    }

    private void notifyRandomize(int numBodies) {
	Event ev = new RandomizeEvent(this, numBodies);
	notifyEvent(ev);
    }

    private void doTest(int numBodies, int computationNumber) {

	notifyParameterChanged();
	notifyRandomize(numBodies);
	try {
	    Thread.sleep(1000);
	} catch (InterruptedException e1) {
	    e1.printStackTrace();
	}
	counter = new CountDownLatch(computationNumber);

	long time1 = System.currentTimeMillis();
	notifyStarted();
	try {
	    counter.await();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	long time2 = System.currentTimeMillis();
	notifyStopped();
	long timeDiff = time2 - time1;
	String out = "sono state eseguite ";
	out += computationNumber;
	out += " computazioni con ";
	out += numBodies + " in ";
	out += timeDiff + " millisecondi";
	System.out.println(out);

    }

    @Override
    public void run() {

	System.out.println("Premere invio iniziare il test");

	try {
	    System.in.read();
	    doTest(500, 500);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	System.out.println("Termino");

	System.exit(0);

    }
}
