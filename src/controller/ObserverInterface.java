package controller;

import nbody.event.Event;


// FIXME: forse questo è l'interfaccia observer
public interface ObserverInterface {

    public void notifyEvent(Event ev);

}