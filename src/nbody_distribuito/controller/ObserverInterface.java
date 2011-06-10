package nbody_distribuito.controller;

import nbody_distribuito.event.Event;

// FIXME: forse questo è l'interfaccia observer
public interface ObserverInterface {

    public void notifyEvent(Event ev);

}