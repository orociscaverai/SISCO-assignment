package nbody;

import nbody.controller.EventHandler;
import nbody.view.ForPerformanceTest;

public class PerformanceTestMain {

    public static void main(String[] args) {

	ForPerformanceTest view = new ForPerformanceTest();
	StateVariables var = new StateVariables();

	StateMonitor state = new StateMonitor();
	EventHandler c = new EventHandler(view, state, var);

	c.start();
	Master m = new Master(view, state, var);
	m.start();
	
	new Thread(view).start();
    }

}
