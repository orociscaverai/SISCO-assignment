package nbody.common;

import nbody.event.Event;

public interface ObserverInterface {

    public void notifyEvent(Event ev);

}