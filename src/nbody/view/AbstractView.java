package nbody.view;

import nbody.common.ObservableComponent;
import nbody.model.BodiesMap;

public abstract class AbstractView extends ObservableComponent {

    abstract public void setUpdated(BodiesMap map);

    abstract public void setParameter(float deltaTime, float softFactor);

}