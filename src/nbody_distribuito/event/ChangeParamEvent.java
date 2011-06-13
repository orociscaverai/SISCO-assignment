package nbody_distribuito.event;

import nbody_distribuito.view.AbstractView;

public class ChangeParamEvent extends Event {
    private float deltaTime;
    private float softFactor;

    public ChangeParamEvent(AbstractView source, float deltaTime, float softFactor) {
	super("changeParam", source);
	this.deltaTime = deltaTime;
	this.softFactor = softFactor;
    }

    public float getDeltaTime() {
	return deltaTime;
    }

    public float getSoftFactor() {
	return softFactor;
    }
}
