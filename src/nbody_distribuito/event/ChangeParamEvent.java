package nbody_distribuito.event;

import nbody_distribuito.view.NBodyView;

public class ChangeParamEvent extends Event {
    private float deltaTime;
    private float softFactor;

    public ChangeParamEvent(NBodyView source, float deltaTime, float softFactor) {
	super("ChangeParam", source);
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
