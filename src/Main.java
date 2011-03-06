
import gui.NBodyView;
import nbody.Controller;
import nbody.Master;
import nbody.StateMonitor;
import nbody.StateVariables;

public class Main {

    public static void main(String[] args) {

	NBodyView view = new NBodyView(600, 600);
	StateVariables var = new StateVariables();
	StateMonitor state = new StateMonitor();
	Controller c = new Controller(view,state,var);
	c.start();
	Master m = new Master(state,var);
	m.start();
    }

}
