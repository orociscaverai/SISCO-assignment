package nbody_distribuito.controller;

import nbody_distribuito.event.Event;

public interface ObserverInterface {

    public void notifyEvent(Event ev);

}