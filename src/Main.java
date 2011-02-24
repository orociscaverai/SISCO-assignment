import gui.NBodyView;
import nbody.Master;

public class Main {

    public static void main(String[] args) {

	NBodyView view = new NBodyView(600, 600);
	Master master = new Master(view);
	master.start();
    }

}
