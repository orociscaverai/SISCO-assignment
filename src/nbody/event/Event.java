package nbody.event;

import nbody.common.ObservableComponent;

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
