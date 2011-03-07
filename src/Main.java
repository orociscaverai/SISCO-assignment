
import gui.NBodyView;
import nbody.EventHandler;
import nbody.FrameRateUpdater;
import nbody.Master;
import nbody.StateMonitor;
import nbody.StateVariables;

public class Main {

    public static void main(String[] args) {

	NBodyView view = new NBodyView(600, 600);
	StateVariables var = new StateVariables();

	StateMonitor state = new StateMonitor();
	EventHandler c = new EventHandler(view,state,var);
	c.start();
	Master m = new Master(state,var);
	m.start();
	FrameRateUpdater u = new FrameRateUpdater(var);
	u.addListener(view);
	u.start();	
    }

}
