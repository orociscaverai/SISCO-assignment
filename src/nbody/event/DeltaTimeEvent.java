package nbody.event;

import gui.NBodyView;

public class DeltaTimeEvent extends Event {
    private float deltaTime;

    public DeltaTimeEvent(NBodyView source, float deltaTime) {
	super("deltaTime", source);
	this.deltaTime = deltaTime;
    }

    public float getDeltaTime() {
	return deltaTime;
    }
}
