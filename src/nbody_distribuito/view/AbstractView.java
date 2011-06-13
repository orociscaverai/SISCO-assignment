package nbody_distribuito.view;

import nbody_distribuito.model.BodiesMap;

/**
 * Classe astratta per la gui
 * @author Boccacci Andrea, Cicora Saverio
 *
 */

public abstract class AbstractView extends ObservableComponent {

    abstract public void setUpdated(BodiesMap map);

    abstract public void setParameter(float deltaTime, float softFactor);
    
    abstract public void setState(String state);

}