package nbody_distribuito.event;

import nbody_distribuito.view.ObservableComponent;

public class Event {

    private String descr;
    private ObservableComponent source;

    public Event(String descr, ObservableComponent source) {
	this.descr = descr;
	this.source = source;
    }

    public ObservableComponent getSource() {
	return source;
    }

    public String getDescription() {
	return descr;
    }
}
