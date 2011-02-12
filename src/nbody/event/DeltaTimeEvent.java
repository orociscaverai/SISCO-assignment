package nbody.event;

import gui.NBodyView;

public class DeltaTimeEvent extends Event {
    private float deltaTime;
    private float softFactor;

    public DeltaTimeEvent(NBodyView source, float deltaTime, float softFactor) {
	super("deltaTime", source);
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
