package nbody.view.swing;

import nbody.BodiesMap;

public abstract class AbstractView extends ObservableComponent {

    abstract public void setUpdated(BodiesMap map);

    abstract public void setProcessingState();

    abstract public void setCompletedState(long dt);

    abstract public void setInterruptedState(long dt);

    abstract public void setParameter(float deltaTime, float softFactor);

}