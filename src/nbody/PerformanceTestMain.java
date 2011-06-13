package nbody;

import nbody.controller.EventHandler;
import nbody.controller.Master;
import nbody.model.StateMonitor;
import nbody.model.StateVariables;
import nbody.view.PerformanceTest;

public class PerformanceTestMain {

    public static void main(String[] args) {

	PerformanceTest view = new PerformanceTest();
	StateVariables var = new StateVariables();

	StateMonitor state = new StateMonitor();
	EventHandler c = new EventHandler(view, state, var);

	c.start();
	Master m = new Master(view, state, var);
	m.start();
	
	new Thread(view).start();
    }

}
