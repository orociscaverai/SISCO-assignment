package nbody;

import nbody.controller.EventHandler;
import nbody.controller.Master;
import nbody.model.StateMonitor;
import nbody.model.StateVariables;
import nbody.view.swing.NBodyView;

public class Main {

    public static void main(String[] args) {

	NBodyView view = new NBodyView(600, 600);
	StateVariables var = new StateVariables();

	StateMonitor state = new StateMonitor();
	EventHandler c = new EventHandler(view, state, var);

	c.start();
	Master m = new Master(view, state, var);
	m.start();
    }

}
