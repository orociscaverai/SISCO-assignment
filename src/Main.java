import gui.NBodyView;
import nbody.Controller;

public class Main {

    public static void main(String[] args) {

	NBodyView view = new NBodyView(600, 600);
	Controller c = new Controller(view);
	c.start();
    }

}
